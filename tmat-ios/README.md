# Tnk Tmat SDK iOS 샘플

- `TmatSample.xcodeproj` 를 Xcode 로 열어 실행합니다. SDK 는 `sdk/TnkTmatSdk.xcframework` 를 Embed & Sign 으로 연결해 두었습니다.
- `TmatSample/Info.plist` 의 `tnkad_app_id` 를 Tnk 사이트의 APP ID 로 바꾸세요.
- 앱 추적 동의(ATT) 후 `applicationStarted()` 를 호출하고, 버튼으로 `actionCompleted` · `buyCompleted` 를 호출합니다.
- **실제 기기에서 확인하세요.** 시뮬레이터는 광고 식별자가 `0000…` 이라 실행·액션이 전송되지 않습니다.
- 프로젝트는 `project.yml` 로 생성했습니다(`xcodegen generate`). 가이드: [iOS_Guide.md](../iOS_Guide.md)
