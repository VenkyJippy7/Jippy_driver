package com.jippymart.driver

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import com.google.android.play.core.integrity.*

class MainActivity: FlutterActivity() {
    private val CHANNEL = "play_integrity_channel"

    override fun configureFlutterEngine(flutterEngine: io.flutter.embedding.engine.FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "getIntegrityToken") {
                val integrityManager = IntegrityManagerFactory.create(applicationContext)
                val request = IntegrityTokenRequest.builder()
                    .setNonce("random-nonce") // You can customize this
                    .build()

                integrityManager.requestIntegrityToken(request)
                    .addOnSuccessListener { response ->
                        result.success(response.token())
                    }
                    .addOnFailureListener { e ->
                        result.error("INTEGRITY_ERROR", e.localizedMessage, null)
                    }
            } else {
                result.notImplemented()
            }
        }
    }
} 