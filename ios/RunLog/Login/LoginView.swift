import RunLogShared
import SwiftUI

struct LoginView: View {
    @ObservedObject var viewModel = LoginViewModel()
    @EnvironmentObject var userAuth: UserAuth

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
                    case .idle:
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
        }.onReceive(viewModel.$state, perform: { state in
            if state == .success {
                userAuth.onLoginSuccess()
            }
        })
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView(viewModel: LoginViewModel())
    }
}
