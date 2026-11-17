# Tnk Tmat SDK (iOS) 연동 가이드

Tnk Tmat SDK 는 광고주 앱에 탑재하여 **앱 실행**, **사용자 액션(CPE)**, **구매** 이벤트를 Tnk 서버로 전달하는 SDK 입니다.
실행형·액션형(CPE) 광고의 참여 확인과 Tnk 사이트의 분석 보고서에 사용됩니다.

| 항목 | 내용 |
|------|------|
| 최신 버전 | **6.0.0** (2026.11.17) |
| 최소 iOS | 15.0 |
| 언어 | Swift · Objective-C |
| 샘플 프로젝트 | [`tmat-ios/`](./tmat-ios) |

## 목차

1. [SDK 설정하기](#1-sdk-설정하기)
   * [라이브러리 다운로드](#라이브러리-다운로드)
   * [라이브러리 등록](#라이브러리-등록)
   * [Info.plist 설정하기](#infoplist-설정하기)
   * [앱 추적 동의 (ATT)](#앱-추적-동의-att)
2. [Analytics Report](#2-analytics-report)
   * [필수 호출 — applicationStarted()](#applicationstarted)
   * [사용 활동 분석 — actionCompleted(actionName:)](#actioncompletedactionname)
   * [구매 활동 분석 — buyCompleted(itemName:)](#buycompleteditemname)
   * [사용자 정보 설정](#사용자-정보-설정)
3. [Objective-C](#3-objective-c)
4. [연동 확인](#4-연동-확인)
5. [참고](#5-참고)

## 1. SDK 설정하기

### 라이브러리 다운로드

**[[iOS Tmat SDK Download v6.0.0](./tmat-ios/sdk/TnkTmatSdk.v6.0.0.zip)]**

### 라이브러리 등록

1. 다운로드한 압축 파일을 풀면 `TnkTmatSdk.xcframework` 가 나옵니다.
2. `TnkTmatSdk.xcframework` 를 Xcode 프로젝트로 드래그합니다.
3. **Target → General → Frameworks, Libraries, and Embedded Content** 에서 `TnkTmatSdk.xcframework` 의 Embed 설정을 **Embed & Sign** 으로 바꿉니다.

다른 라이브러리는 필요 없습니다(시스템 프레임워크만 사용). 개인정보 매니페스트(`PrivacyInfo.xcprivacy`)는 SDK 에 포함되어 있습니다.

### Info.plist 설정하기

Tnk 사이트에서 앱을 등록하면 앱 정보 상단에 **APP ID** 가 표시됩니다. 이 값을 `Info.plist` 에 넣습니다. 표시된 그대로(하이픈 포함) 넣으시면 됩니다.

> APP ID 옆의 **APP KEY** 는 서버 간 포스트백 검증용 값입니다. Info.plist 에는 **APP ID** 를 넣습니다.

| Key | Type | 값 |
|-----|------|----|
| `tnkad_app_id` | String | Tnk 사이트의 APP ID |
| `tnkad_tracking` | Boolean | 재방문 등 사용 활동 수집 여부 (선택, 기본값 NO) |
| `NSUserTrackingUsageDescription` | String | 앱 추적 동의 팝업에 표시할 문구 (필수) |

```xml
<key>tnkad_app_id</key>
<string>your-app-id-from-tnk-site</string>
<key>tnkad_tracking</key>
<true/>
<key>NSUserTrackingUsageDescription</key>
<string>광고 참여 확인을 위해 광고 식별자를 사용합니다.</string>
```

App ID 를 Info.plist 대신 코드에서 지정하려면 다른 API 보다 먼저 아래를 호출합니다.

```swift
TnkSession.initInstance(appId: "your-app-id-from-tnk-site")
```

### 앱 추적 동의 (ATT)

광고 참여 여부를 확인하려면 기기의 광고 식별자(IDFA)가 필요하고, IDFA 를 얻으려면 iOS 14 부터 **앱 추적 동의**를 받아야 합니다.

- **사용자가 추적을 허용하지 않으면 SDK 는 실행·액션 정보를 서버로 보내지 않습니다.** 보내지 않은 정보는 기록되지 않으므로, 이후 사용자가 설정에서 추적을 허용한 뒤 다시 호출되면 그때 전송됩니다.
- 동의 팝업은 앱이 활성(active) 상태일 때만 표시됩니다. 앱 시작 직후 첫 화면이 나타난 뒤 요청하세요.

```swift
import AppTrackingTransparency

ATTrackingManager.requestTrackingAuthorization { status in
    // 동의 결과가 나온 뒤 실행을 알립니다.
    TnkSession.sharedInstance()?.applicationStarted()
}
```

## 2. Analytics Report

### 필수 호출

#### applicationStarted()

##### Method

- `@objc public func applicationStarted()`

##### Description

앱이 실행되었음을 알립니다. **다른 API 보다 먼저** 호출되어야 합니다.
[앱 추적 동의](#앱-추적-동의-att) 결과를 받은 뒤 호출하는 것을 권장합니다.

- 실행 정보는 **기기당 최초 1회만** 서버로 전송됩니다. 앱을 실행할 때마다 호출해도 됩니다.
- `tnkad_tracking` 이 켜져 있으면 이후 실행 때는 재방문 정보가 전송됩니다.
- 서버 통신은 SDK 내부에서 백그라운드로 처리되므로 메인 스레드에서 바로 호출해도 됩니다.

##### 적용 예시

```swift
import TnkTmatSdk

TnkSession.sharedInstance()?.applicationStarted()
```

### 사용 활동 분석

사용자가 앱을 설치하고 처음 실행한 뒤 어떤 행동을 하는지 분석하거나, **액션형(CPE) 광고의 참여 완료**를 알릴 때 사용합니다.

#### actionCompleted(actionName:)

##### Method

- `@objc public func actionCompleted(actionName: String)`

##### Description

사용자의 특정 액션이 완료된 시점에 호출합니다.

- **같은 액션은 기기당 최초 1회만** 전송됩니다. 완료 시점마다 호출해도 중복 전송되지 않습니다.
- 통신에 실패하면 기록하지 않고 다음 호출 때 다시 전송합니다.

##### Parameters

| 파라미터 | 내용 |
|----------|------|
| actionName | 사용자 액션을 구별하기 위한 문자열 (예: `"signup_completed"`)<br>사용하는 actionName 은 모두 **Tnk 사이트에 등록**되어야 합니다.<br>액션형(CPE) 광고는 캠페인에 등록된 액션명과 **같은 문자열**을 사용해야 참여가 인정됩니다. 액션명은 Tnk 담당자와 협의해 확정하세요. |

##### 적용 예시

```swift
// 회원 가입 완료 시
TnkSession.sharedInstance()?.actionCompleted(actionName: "signup_completed")

// 튜토리얼 완료 시
TnkSession.sharedInstance()?.actionCompleted(actionName: "tutorial_completed")
```

### 구매 활동 분석

#### buyCompleted(itemName:)

##### Method

- `@objc public func buyCompleted(itemName: String)`

##### Description

사용자가 유료 구매를 완료했을 때 호출합니다. 유입 경로별 구매 횟수·구매 사용자 수 등을 분석할 수 있습니다.

##### 적용 예시

```swift
TnkSession.sharedInstance()?.buyCompleted(itemName: "item_01")
```

### 사용자 정보 설정

모두 선택 사항입니다.

```swift
// 앱의 사용자 식별값 (최대 256 byte)
TnkSession.sharedInstance()?.setUserName("user_id")

// 재방문 추적 사용 여부 (Info.plist 의 tnkad_tracking 대신 코드로 정할 때)
TnkSession.sharedInstance()?.setTrackingEnabled(true)
```

## 3. Objective-C

Objective-C 에서는 클래스 이름이 **`TnkTmatSession`** 입니다.

```objc
@import TnkTmatSdk;

[[TnkTmatSession sharedInstance] applicationStarted];
[[TnkTmatSession sharedInstance] actionCompletedWithActionName:@"tutorial_completed"];
[[TnkTmatSession sharedInstance] buyCompletedWithItemName:@"item_01"];
[[TnkTmatSession sharedInstance] setUserName:@"user_id"];
```

## 4. 연동 확인

1. 연동 확인 중에만 로그를 켭니다. 출시 빌드에서는 호출하지 않거나 `false` 로 설정하세요.

   ```swift
   TnkSession.enableLogging(true)
   ```

2. Xcode 콘솔에서 **`[TNK_AD]`** 로 확인합니다.

   | 로그 | 뜻 |
   |------|----|
   | `sdk version : 6.0.0` | SDK 가 초기화되었습니다 |
   | `request for payment returned. return code = …` | 서버가 응답했습니다 |
   | `… 보류: 광고 ID(IDFA)가 없습니다 …` | 추적이 허용되지 않아 전송하지 않았습니다 |
   | `RPFS …` / `RPFA …` | 실행(RPFS)·액션(RPFA) 전송 중 오류입니다. 로그와 함께 Tnk 담당자에게 알려 주세요 |

3. **실제 기기에서 테스트하세요.** iOS 시뮬레이터는 추적을 허용해도 광고 식별자가 `0000…` 이라 실행·액션이 전송되지 않습니다.
4. 테스트 기기에서 실행·액션이 정상 수신되는지는 Tnk 담당자에게 확인을 요청해 주세요.
   - 액션은 기기당 최초 1회만 전송됩니다. SDK 는 전송 기록을 앱 데이터에 저장하므로, 다시 테스트하려면 앱을 삭제 후 재설치하세요.

## 5. 참고

- **Tnk Reward SDK(TnkRwdSdk2)와 함께 사용하는 경우**: 두 SDK 모두 `TnkSession` 이라는 Swift 이름을 쓰므로 한 파일에서 둘 다 import 하면 `TnkTmatSdk.TnkSession` 처럼 모듈 이름을 붙여 구분하세요. Objective-C 이름은 `TnkTmatSession` 으로 달라 충돌하지 않습니다.
- **개인정보 처리 명세(App Store Connect)**: SDK 는 광고 식별자(IDFA)·기기 정보·앱이 설정한 사용자 식별값을 광고 참여 확인 목적으로 Tnk 서버에 전송합니다. 포함된 `PrivacyInfo.xcprivacy` 를 참고해 앱의 개인정보 항목을 작성하세요.
