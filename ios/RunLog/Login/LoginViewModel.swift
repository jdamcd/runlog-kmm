import AuthenticationServices
import Foundation
import RunLogShared

class LoginViewModel: NSObject, ObservableObject, ASWebAuthenticationPresentationContextProviding {
    @Published var state: LoginState = .idle

    private let stravaLogin: StravaLogin

    init(stravaLogin: StravaLogin = SharedModule().stravaLogin()) {
        self.stravaLogin = stravaLogin
    }

    @MainActor
    func startLogin() {
        state = .loading
        let loginUrl = URL(string: stravaLogin.loginUrl)!
        let session = ASWebAuthenticationSession(
            url: loginUrl,
            callbackURLScheme: stravaLogin.authScheme
        ) { result, error in
            if let result {
                self.handleCallback(result: result)
            } else if error != nil {
                self.state = .idle
            }
        }
        session.presentationContextProvider = self
        session.prefersEphemeralWebBrowserSession = false
        session.start()
    }

    @MainActor
    private func handleCallback(result: URL) {
        if let code = result.paramValue("code"), let scope = result.paramValue("scope") {
            if scope.contains("activity:read_all") {
                submitAuthCode(code: code)
            } else {
                state = .permission_error
            }
        } else {
            state = .idle
        }
    }

    @MainActor
    private func submitAuthCode(code: String) {
        state = .loading
        Task {
            let result = try await stravaLogin.authenticate(code: code)
            if result is LoginResult.Success {
                state = .success
            } else {
                state = .idle
            }
        }
    }

    func presentationAnchor(for _: ASWebAuthenticationSession) -> ASPresentationAnchor {
        ASPresentationAnchor()
    }
}

enum LoginState {
    case idle
    case loading
    case success
    case permission_error
}
