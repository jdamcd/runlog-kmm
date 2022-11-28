import RunLogShared
import SwiftUI

struct LoginView: View {
    @ObservedObject var viewModel = LoginViewModel()
    @EnvironmentObject var userAuth: UserAuth
    @State private var showingPermissionError = false

    var body: some View {
        ZStack {
            Image("LoginBackground")
                .resizable()
                .aspectRatio(UIImage(named: "LoginBackground")!.size, contentMode: .fill)
                .edgesIgnoringSafeArea(.all)

            VStack {
                Spacer()
                Image("Logo")
                    .shadow(radius: 4)
                Spacer()
                ZStack {
                    switch viewModel.state {
                    case .loading:
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color.white))
                    case .idle, .permission_error:
                        Button(action: {
                            viewModel.startLogin()
                        }) {
                            Image("ConnectButton")
                        }
                    default:
                        EmptyView()
                    }
                }.frame(height: 60)
                Spacer()
                Image("StravaPowered")
            }.frame(maxHeight: .infinity)
                .padding(.vertical, 30)
                .alert(Copy.login_permission_error,
                       isPresented: $showingPermissionError) {
                    Button(Copy.button_ok) { showingPermissionError = false }
                }
        }.onReceive(viewModel.$state) { state in
            if state == .success {
                userAuth.onLoginSuccess()
            } else if state == .permission_error {
                showingPermissionError = true
            }
        }
        .statusBarHidden()
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView(viewModel: LoginViewModel())
    }
}
