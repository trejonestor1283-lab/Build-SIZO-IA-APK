<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# Run and deploy your AI Studio app

This contains everything you need to run your app locally.

View your app in AI Studio: https://ai.studio/apps/1472c47e-b71c-4e53-9884-6fe10095c824

## Run Locally

**Prerequisites:**  [Android Studio](https://developer.android.com/studio)


1. Open Android Studio
2. Select **Open** and choose the directory containing this project
3. Allow Android Studio to fix any incompatibilities as it imports the project.
4. Create a file named `.env` in the project directory and set `GEMINI_API_KEY` in that file to your Gemini API key (see `.env.example` for an example)
5. Remove this line from the app's `build.gradle.kts` file: `signingConfig = signingConfigs.getByName("debugConfig")`
6. Run the app on an emulator or physical device
7. If you have already published your app in AI Studio, please [request upload key reset](https://support.google.com/googleplay/android-developer/answer/9842756#zippy=%2Crequest-an-upload-key-reset) in Google Play Console.


## Gemini connection and troubleshooting

SIZO uses the Gemini API with the selected model. If the request fails, the chat shows a real connection/configuration error rather than returning a fabricated local answer. Configure `GEMINI_API_KEY` through the build secrets or paste a key in Settings for local testing. Do not publish an APK containing a private API key: production builds should call Gemini through a protected backend. HTTP 502/503/504 can be temporary upstream or proxy failures; 401/403 usually indicate key or permission issues, 404 usually indicates a model/endpoint mismatch, and 429 indicates rate/quota limits.


## Build an APK in GitHub Actions (phone-friendly)

1. Upload this project folder's contents to a GitHub repository (not the ZIP itself).
2. Open the repository's **Actions** tab and choose **Build SIZO IA APK**.
3. Press **Run workflow**. Wait for the run to finish successfully.
4. Open the successful run and download the artifact named **SIZO-IA-debug-apk**. Unzip it to get `app-debug.apk`.
5. Install the APK on Android. The first time, paste your Gemini API key in Ajustes.

This workflow builds a debug APK; it does not prove Gemini availability. Test the app with a valid API key after installation. Do not hard-code a personal API key into the APK; a production release should call Gemini through a protected backend.
