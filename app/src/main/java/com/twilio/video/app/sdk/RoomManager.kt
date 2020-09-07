package com.twilio.video.app.sdk

import android.content.Context
import android.content.SharedPreferences
import com.twilio.video.Participant
import com.twilio.video.RemoteParticipant
import com.twilio.video.Room
import com.twilio.video.TwilioException
import com.twilio.video.app.Apis.ClassesAPI
import com.twilio.video.app.BuildConfig
import com.twilio.video.app.data.api.AuthServiceError
import com.twilio.video.app.data.api.AuthServiceException
import com.twilio.video.app.ui.room.RoomEvent
import com.twilio.video.app.ui.room.RoomEvent.*
import com.twilio.video.app.ui.room.RoomEvent.ParticipantEvent.ParticipantConnected
import com.twilio.video.app.ui.room.RoomEvent.ParticipantEvent.ParticipantDisconnected
import com.twilio.video.app.ui.room.VideoService.Companion.startService
import com.twilio.video.app.ui.room.VideoService.Companion.stopService
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

const val MICROPHONE_TRACK_NAME = "microphone"
const val CAMERA_TRACK_NAME = "camera"
const val SCREEN_TRACK_NAME = "screen"

class RoomManager(
    private val context: Context,
    private val videoClient: VideoClient
) {

    private val roomListener = RoomListener()
    private val roomEventSubject = PublishSubject.create<RoomEvent>()
    var room: Room? = null
    val roomEvents: Observable<RoomEvent> = roomEventSubject

    fun disconnect() {
        room?.disconnect()
    }

    suspend fun connect(identity: String, roomName: String) {
        roomEventSubject.onNext(Connecting)
        room = try {
            videoClient.connect(identity, roomName, roomListener)
        } catch (e: AuthServiceException) {
            handleTokenException(e, e.error)
        } catch (e: Exception) {
            handleTokenException(e)
        }
    }

    fun sendParticipantEvent(participantEvent: ParticipantEvent) {
        roomEventSubject.onNext(participantEvent)
    }

    private fun handleTokenException(e: Exception, error: AuthServiceError? = null): Room? {
        Timber.e(e, "Failed to retrieve token")
        roomEventSubject.onNext(RoomEvent.TokenError(serviceError = error))
        return null
    }

    inner class RoomListener : Room.Listener {
        override fun onConnected(room: Room) {
            Timber.i("onConnected -> room sid: %s",
                    room.sid)

            startService(context, room.name)

            setupParticipants(room)
        }

        override fun onDisconnected(room: Room, twilioException: TwilioException?) {
            Timber.i("Disconnected from room -> sid: %s, state: %s",
                    room.sid, room.state)

            //callDsiconnectApi()

            stopService(context)

            roomEventSubject.onNext(Disconnected)

        }



        override fun onConnectFailure(room: Room, twilioException: TwilioException) {
            Timber.e(
                    "Failed to connect to room -> sid: %s, state: %s, code: %d, error: %s",
                    room.sid,
                    room.state,
                    twilioException.code,
                    twilioException.message)
            roomEventSubject.onNext(ConnectFailure)

        }

        override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
            Timber.i("RemoteParticipant connected -> room sid: %s, remoteParticipant: %s",
                    room.sid, remoteParticipant.sid)

            remoteParticipant.setListener(RemoteParticipantListener(this@RoomManager))
            sendParticipantEvent(ParticipantConnected(remoteParticipant))
        }

        override fun onParticipantDisconnected(room: Room, remoteParticipant: RemoteParticipant) {
            Timber.i("RemoteParticipant disconnected -> room sid: %s, remoteParticipant: %s",
                    room.sid, remoteParticipant.sid)

            sendParticipantEvent(ParticipantDisconnected(remoteParticipant.sid))
        }

        override fun onDominantSpeakerChanged(room: Room, remoteParticipant: RemoteParticipant?) {
            Timber.i("DominantSpeakerChanged -> room sid: %s, remoteParticipant: %s",
                    room.sid, remoteParticipant?.sid)

            roomEventSubject.onNext(DominantSpeakerChanged(remoteParticipant?.sid))
        }

        override fun onRecordingStarted(room: Room) {}

        override fun onReconnected(room: Room) {
            Timber.i("onReconnected: %s", room.name)
        }

        override fun onReconnecting(room: Room, twilioException: TwilioException) {
            Timber.i("onReconnecting: %s", room.name)
        }

        override fun onRecordingStopped(room: Room) {}

        private fun setupParticipants(room: Room) {
            room.localParticipant?.let { localParticipant ->
                val participants = mutableListOf<Participant>()
                participants.add(localParticipant)
                localParticipant.setListener(LocalParticipantListener(this@RoomManager))

                room.remoteParticipants.forEach {
                    it.setListener(RemoteParticipantListener(this@RoomManager))
                    participants.add(it)
                }

                roomEventSubject.onNext(Connected(participants, room, room.name))
            }
        }
    }

    private fun callDsiconnectApi() {
        val client = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.virtualskill.in/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

         val classDisconnect = retrofit.create(ClassesAPI::class.java)

        val sharedPreferences: SharedPreferences = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);

        val token = sharedPreferences.getString("token","").toString();

        val call = classDisconnect.endClass(token)
        val result = call.execute().body()


    }


}
