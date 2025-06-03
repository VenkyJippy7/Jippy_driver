import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:driver/constant/constant.dart';
import 'package:flutter/services.dart';

class PlayIntegrityService {
  static const _channel = MethodChannel('play_integrity_channel');

  static Future<String?> getIntegrityToken() async {
    try {
      final token = await _channel.invokeMethod<String>('getIntegrityToken');
      return token;
    } on PlatformException catch (e) {
      print('Integrity API error: ${e.message}');
      return null;
    }
  }

  static Future<bool> verifyIntegrity(String token) async {
    try {
      final response = await http.post(
        Uri.parse('${Constant.apiUrl}/api/verify-integrity'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'token': token,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['success'] ?? false;
      }
      return false;
    } catch (e) {
      print('Error verifying integrity: $e');
      return false;
    }
  }
} 