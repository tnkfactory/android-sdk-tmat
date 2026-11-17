# Tnk mobile app tracking SDK

Tnk Tmat SDK는 아래의 기능들을 사용하실 수 있습니다.

* Analytics Report — 앱 실행 · 사용자 액션(CPE) · 구매 이벤트 전달

| 플랫폼 | 가이드 | 샘플 프로젝트 | 최신 버전 |
|--------|--------|---------------|-----------|
| Android | [Android 가이드](./Android_Guide.md) | [`tmat-android/`](./tmat-android) | 8.09.05 |
| iOS | [iOS 가이드](./iOS_Guide.md) | [`tmat-ios/`](./tmat-ios) | 6.0.0 |
| Unity (Android) | [Unity 플러그인 가이드](./Unity_Plugin_Guide.md) | — | 7.03.3 |

## Android

### Android Guide

[안드로이드 가이드 문서](./Android_Guide.md) · 샘플: [`tmat-android/`](./tmat-android)

### Update Notice

* 2026.11.17
  * Android SDK v8.09.05 업데이트
    * 앱 ProGuard/R8 설정에 Tnk 규칙(`-keep class com.tnkfactory.** { *;}`)이 더 이상 필요 없음 — SDK 에 포함되어 자동 적용
    * 빌드 환경 업데이트 (AGP 9.1.1)
    * 저장소 주소 변경 권장: `https://repository.tnkad.net/repository/public/` ([가이드](./Android_Guide.md#라이브러리-등록) 참고)
    * API 변경 없음
* 2025.07.18
  * Android SDK v8.09.04 업데이트
  * google play service 라이브러리 업데이트
* 2021.04.15
  * Android SDK v7.03.3 업데이트
    * 배포 저장소 Maven Central 로 이전
* 2020.12.14
  * Android SDK v7.02.3 업데이트

## iOS

### iOS Guide

[iOS 가이드 문서](./iOS_Guide.md) · 샘플: [`tmat-ios/`](./tmat-ios) · SDK 다운로드: [TnkTmatSdk v6.0.0](./tmat-ios/sdk/TnkTmatSdk.v6.0.0.zip)

### Update Notice

* 2026.11.17
  * iOS SDK v6.0.0 최초 배포
    * `applicationStarted()` · `actionCompleted(actionName:)` · `buyCompleted(itemName:)` (Android 와 같은 동작)
    * 최소 iOS 15.0, Swift · Objective-C(`TnkTmatSession`) 지원

## Unity

### Unity Plugin Guide

[유니티 플러그인 가이드 문서](./Unity_Plugin_Guide.md)

### Update Notice

* 2021.04.15
  * Android SDK v7.03.3 적용
* 2020.12.15
  * Android SDK v7.02.3 적용


