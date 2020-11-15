import Foundation
import AuthenticationServices
import RunLogShared

class LoginViewModel: NSObject, ObservableObject, ASWebAuthenticationPresentationContextProviding {
    
    @Published var state: LoginState = .idle
    
    private let strava: Strava
    
    init(strava: Strava = SharedModule().buildStrava(userState: UserState())) {
        self.strava = strava
    }

    func startLogin() {
        state = .loading
        let loginUrl = URL(string: strava.loginUrl)!
        let session = ASWebAuthenticationSession(
            url: loginUrl,
            callbackURLScheme: strava.authRedirect) { (result, error) in
            if let result = result {
                if let code = result.paramValue("code") {
                    self.submitAuthCode(code: code)
                } else {
                    self.state = .idle
                }
            }
            if error != nil {
                self.state = .idle
            }
        }
        session.presentationContextProvider = self
        session.prefersEphemeralWebBrowserSession = false
        session.start()
    }
    
    private func submitAuthCode(code: String) {
        state = .loading
        strava.authenticate(code: code) { (result, _) in
            if result is LoginResult.Success {
                print("Login success")
                self.state = .success
            } else {
                self.state = .idle
            }
        }
    }
    
    func presentationAnchor(for session: ASWebAuthenticationSession) -> ASPresentationAnchor {
        return ASPresentationAnchor()
    }
}

enum LoginState {
    case idle
    case loading
    case success
}
