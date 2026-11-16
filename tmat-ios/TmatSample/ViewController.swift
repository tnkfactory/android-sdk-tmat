import UIKit
import AppTrackingTransparency
import TnkTmatSdk

class ViewController: UIViewController {

    private var didRequestTracking = false

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Tnk Tmat Sample"
        view.backgroundColor = .systemBackground

        // 연동 확인용 로그(Xcode 콘솔 [TNK_AD]). 출시 빌드에서는 끄세요.
        TnkSession.enableLogging(true)

        // 유저 식별 값 설정(선택)
        TnkSession.sharedInstance()?.setUserName("Test_User")

        let stack = UIStackView(arrangedSubviews: [
            button("Action Completed Test 01") { TnkSession.sharedInstance()?.actionCompleted(actionName: "test_01") },
            button("Action Completed Test 02") { TnkSession.sharedInstance()?.actionCompleted(actionName: "test_02") },
            button("Buy Completed Test 01") { TnkSession.sharedInstance()?.buyCompleted(itemName: "item_01") },
            button("Buy Completed Test 02") { TnkSession.sharedInstance()?.buyCompleted(itemName: "item_02") },
        ])
        stack.axis = .vertical
        stack.spacing = 12
        stack.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(stack)
        NSLayoutConstraint.activate([
            stack.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 20),
            stack.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -20),
            stack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 20),
        ])
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        guard !didRequestTracking else { return }
        didRequestTracking = true

        // ATT 팝업은 앱이 활성 상태일 때만 뜬다. 결과가 나온 뒤 실행을 알린다.
        ATTrackingManager.requestTrackingAuthorization { status in
            // authorized(3) 가 아니면 광고 ID 를 얻을 수 없어 SDK 가 실행·액션 전송을 보류한다.
            print("ATT status = \(status.rawValue)")
            // 앱 실행 알림 — 다른 Tnk API 보다 먼저 호출
            TnkSession.sharedInstance()?.applicationStarted()
        }
    }

    private func button(_ title: String, _ action: @escaping () -> Void) -> UIButton {
        let b = UIButton(type: .system)
        b.setTitle(title, for: .normal)
        b.titleLabel?.font = .systemFont(ofSize: 17)
        b.backgroundColor = .secondarySystemBackground
        b.layer.cornerRadius = 8
        b.heightAnchor.constraint(equalToConstant: 48).isActive = true
        b.addAction(UIAction { _ in action() }, for: .touchUpInside)
        return b
    }
}
