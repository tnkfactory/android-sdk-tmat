# Tnk Tmat SDK (Android) 연동 가이드

Tnk Tmat SDK 는 광고주 앱에 탑재하여 **앱 실행**, **사용자 액션(CPE)**, **구매** 이벤트를 Tnk 서버로 전달하는 SDK 입니다.
실행형·액션형(CPE) 광고의 참여 확인과 Tnk 사이트의 분석 보고서에 사용됩니다.

| 항목 | 내용 |
|------|------|
| 최신 버전 | **8.09.05** (2026.11.16) |
| 최소 Android | API 21 (Android 5.0) |
| 저장소 | `https://repository.tnkad.net/repository/public/` |
| 샘플 프로젝트 | [`tmat-android/`](./tmat-android) |

## 목차

1. [SDK 설정하기](#1-sdk-설정하기)
   * [라이브러리 등록](#라이브러리-등록)
   * [Manifest 설정하기](#manifest-설정하기)
   * [R8 / ProGuard](#r8--proguard)
2. [Analytics Report](#2-analytics-report)
   * [필수 호출 — TnkSession.applicationStarted()](#tnksessionapplicationstarted)
   * [사용 활동 분석 — TnkSession.actionCompleted()](#tnksessionactioncompleted)
   * [구매 활동 분석 — TnkSession.buyCompleted()](#tnksessionbuycompleted)
   * [사용자 정보 설정](#사용자-정보-설정)
3. [연동 확인](#3-연동-확인)
4. [이전 버전에서 업데이트](#4-이전-버전에서-업데이트)

## 1. SDK 설정하기

### 라이브러리 등록

Tnk SDK 는 Tnk 저장소에 배포되어 있습니다. `settings.gradle(.kts)` 의 `dependencyResolutionManagement` 에 저장소를 추가합니다.

**Kotlin DSL (`settings.gradle.kts`)**

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Tnk SDK 저장소
        maven { url = uri("https://repository.tnkad.net/repository/public/") }
    }
}
```

**Groovy (`settings.gradle`)**

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Tnk SDK 저장소
        maven { url 'https://repository.tnkad.net/repository/public/' }
    }
}
```

> `settings.gradle` 에 `dependencyResolutionManagement` 가 없는 프로젝트는 최상위 `build.gradle` 의 `allprojects { repositories { ... } }` 에 같은 주소를 추가하세요.

> **기존에 `https://repository.tnkad.net:8443/repository/public/` 주소를 사용 중이라면 위 주소(포트 번호 없음)로 바꿔 주세요.**
> 일부 JDK(예: JDK 17) 환경에서 8443 주소는 `PKIX path building failed` 인증서 오류로 SDK 를 내려받지 못할 수 있습니다.

App 모듈의 `build.gradle(.kts)` 에 의존성을 추가합니다.

```kotlin
dependencies {
    implementation("com.tnkfactory:tmat:8.09.05")
}
```

```gradle
dependencies {
    implementation 'com.tnkfactory:tmat:8.09.05'
}
```

Google Play Install Referrer 라이브러리 등 SDK 가 필요로 하는 라이브러리는 함께 자동으로 추가됩니다.

### Manifest 설정하기

#### App ID 설정

Tnk 사이트에서 앱을 등록하면 앱 정보 상단에 **APP ID** 가 표시됩니다.
이 값을 `AndroidManifest.xml` 의 `<application>` 태그 안에 설정합니다. 표시된 그대로(하이픈 포함) 넣으시면 됩니다.

> APP ID 옆의 **APP KEY** 는 서버 간 포스트백 검증용 값입니다. Manifest 에는 **APP ID** 를 넣습니다.

`tnkad_tracking` 을 `true` 로 설정하면 재방문 등 사용 활동 데이터가 수집됩니다. 수집을 원하지 않으면 `false` 로 설정하세요.

```xml
<application>
    ...
    <meta-data android:name="tnkad_app_id" android:value="your-app-id-from-tnk-site" />
    <meta-data android:name="tnkad_tracking" android:value="true" />
    ...
</application>
```

App ID 를 Manifest 대신 코드에서 지정하려면 다른 API 보다 먼저 아래를 호출합니다.

```kotlin
TnkSession.initInstance(context, "your-app-id-from-tnk-site")
```

#### 권한

SDK 에 필요한 권한은 SDK 에 포함되어 있어 **앱 Manifest 에 따로 추가할 필요가 없습니다.** 빌드 시 자동으로 병합됩니다.

| 권한 | 용도 |
|------|------|
| `android.permission.INTERNET` | 서버 통신 |
| `android.permission.ACCESS_WIFI_STATE` | 네트워크 상태 확인 |
| `com.google.android.gms.permission.AD_ID` | 광고 ID(ADID) 조회 |
| `com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE` | 유입 경로(Install Referrer) 조회 |

> 유입 경로 파악은 **Google Play 에서 설치한 앱**에 대해서만 제공됩니다.

### R8 / ProGuard

**8.09.05 부터는 앱에 Tnk 관련 ProGuard/R8 규칙을 넣지 않습니다.** 필요한 규칙은 SDK 에 포함되어 자동으로 적용됩니다.

기존 가이드에 따라 아래 규칙을 넣어 두셨다면 **삭제하시기 바랍니다.** 남겨 두어도 동작에는 문제가 없지만, Tnk SDK 코드가 축소·난독화되지 않아 앱 크기와 R8 최적화 효과가 줄어듭니다.

```
# 8.09.05 부터 불필요 — 삭제하세요
-keep class com.tnkfactory.** { *;}
```

> **8.09.04 이하**를 사용하는 경우에는 위 규칙이 반드시 필요합니다. 규칙 없이 R8 을 적용하면 SDK 가 서버 응답을 처리하지 못합니다.

## 2. Analytics Report

Analytics 적용을 위해서는 Tnk 사이트에서 앱 등록 및 [SDK 설정하기](#1-sdk-설정하기)가 먼저 완료되어야 합니다.

### 필수 호출

앱이 실행되는 시점에 `TnkSession.applicationStarted()` 를 호출합니다. 필수 API 이며, 이것만으로도 사용 활동 분석을 제외한 대부분의 분석 데이터를 얻을 수 있습니다.

#### TnkSession.applicationStarted()

##### Method

- `void TnkSession.applicationStarted(Context context)`

##### Description

앱이 시작되는 **첫 화면(Activity)의 `onCreate()`** 에서 호출합니다. 다른 API 보다 먼저 호출되어야 합니다.
서버 통신은 SDK 내부 스레드에서 처리되므로 메인 스레드에서 바로 호출해도 됩니다.

##### Parameters

| 파라미터 | 내용 |
|----------|------|
| context | Context 객체 |

##### 적용 예시

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TnkSession.applicationStarted(this)
    }
}
```

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TnkSession.applicationStarted(this);
    }
}
```

### 사용 활동 분석

사용자가 앱을 설치하고 처음 실행한 뒤 어떤 행동을 하는지 분석하거나, **액션형(CPE) 광고의 참여 완료**를 알릴 때 사용합니다.

예를 들어 회원가입, 튜토리얼 완료, 레벨 달성 등의 행동이 이루어질 때 해당 행동의 구분자와 함께 호출하면
사용자가 어떤 패턴으로 앱을 이용하는지, 어느 단계에서 많이 이탈하는지 등을 분석할 수 있습니다.

#### TnkSession.actionCompleted()

##### Method

- `void TnkSession.actionCompleted(Context context, String actionName)`

##### Description

사용자의 특정 액션이 완료된 시점에 호출합니다.

- **같은 액션은 기기당 최초 1회만** 전송됩니다. 이후 같은 `actionName` 으로 다시 호출해도 전송되지 않으므로 완료 시점마다 호출해도 됩니다.
- `applicationStarted()` 가 먼저 호출된 뒤에 호출되어야 합니다.

##### Parameters

| 파라미터 | 내용 |
|----------|------|
| context | Context 객체 |
| actionName | 사용자 액션을 구별하기 위한 문자열 (예: `"signup_completed"`)<br>사용하는 actionName 은 모두 **Tnk 사이트에 등록**되어야 합니다.<br>액션형(CPE) 광고는 캠페인에 등록된 액션명과 **같은 문자열**을 사용해야 참여가 인정됩니다. 액션명은 Tnk 담당자와 협의해 확정하세요. |

##### 적용 예시

```kotlin
// 회원 가입 완료 시
TnkSession.actionCompleted(this, "signup_completed")

// 튜토리얼 완료 시
TnkSession.actionCompleted(this, "tutorial_completed")
```

```java
// 추가 데이터 다운로드 완료 시
TnkSession.actionCompleted(this, "resource_loaded");

// 친구 추천 시
TnkSession.actionCompleted(this, "friend_invite");
```

### 구매 활동 분석

사용자가 유료 구매 등의 활동을 하는 경우 이에 대한 분석 데이터를 얻으려면 아래 API 를 사용합니다.

유입 경로별 구매 횟수와 구매 사용자 수, 하루 사용자 중 구매 사용자 비율, 첫 실행 후 구매까지 걸리는 시간 등을 분석할 수 있습니다.
보고서 데이터에 아이템별 가격을 대입하면 ARPU·ARPPU 도 산출할 수 있습니다.

#### TnkSession.buyCompleted()

##### Method

- `void TnkSession.buyCompleted(Context context, String itemName)`

##### Description

사용자가 유료 구매를 완료했을 때 호출합니다.

##### Parameters

| 파라미터 | 내용 |
|----------|------|
| context | Context 객체 |
| itemName | 구매한 아이템을 구별하기 위한 문자열 (예: `"item_01"`) |

##### 적용 예시

```kotlin
// item_01 구매 완료 시
TnkSession.buyCompleted(this, "item_01")
```

```java
// item_02 구매 완료 시
TnkSession.buyCompleted(this, "item_02");
```

### 사용자 정보 설정

앱에서 관리하는 사용자 식별값·나이·성별을 설정하면 보고서에 반영됩니다. 모두 선택 사항입니다.

```kotlin
// 앱의 사용자 식별값 (최대 256 byte)
TnkSession.setUserName(this, "user_id")

// 나이
TnkSession.setUserAge(this, 23)

// 성별
TnkSession.setUserGender(this, TnkCode.MALE)   // 남
TnkSession.setUserGender(this, TnkCode.FEMALE) // 여
```

## 3. 연동 확인

1. 연동 확인 중에만 로그를 켭니다. 출시 빌드에서는 호출하지 않거나 `false` 로 설정하세요.

   ```kotlin
   TnkSession.enableLogging(true)
   ```

2. 앱을 실행하고 Logcat 에서 태그 **`TNK_AD`** 로 확인합니다. SDK 가 초기화되면 아래 로그가 출력됩니다.

   ```
   E/TNK_AD: sdk version : 8.09.05
   ```

3. 테스트 기기에서 실행·액션이 정상 수신되는지는 Tnk 담당자에게 확인을 요청해 주세요.
   - 액션은 기기당 최초 1회만 전송됩니다. SDK 는 전송 기록을 앱 데이터에 저장하므로, 다시 테스트하려면 앱 데이터를 삭제하세요.

## 4. 이전 버전에서 업데이트

| 이전 버전 | 할 일 |
|-----------|-------|
| 8.09.04 이하 | ① 의존성 버전을 `8.09.05` 로 변경<br>② 저장소 주소가 `:8443` 이면 [라이브러리 등록](#라이브러리-등록)의 주소로 변경<br>③ 앱 ProGuard/R8 설정의 `-keep class com.tnkfactory.** { *;}` 삭제 ([R8 / ProGuard](#r8--proguard)) |

API 는 8.09.04 와 동일하므로 코드 변경은 필요 없습니다.
