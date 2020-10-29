import SwiftUI
import shared

struct LoginView: View {
    
    @ObservedObject var viewModel = LoginViewModel()
    @EnvironmentObject var userAuth: UserAuth
    
    var body: some View {
        ZStack {
            LinearGradient(gradient: Gradient(
                            colors: [.asset(.primary), .asset(.dark)]),
                           startPoint: .top, endPoint: .bottom)
                .edgesIgnoringSafeArea(.all)
            
            VStack {
                Spacer()
                Image("Logo")
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
                    .padding()
            }.frame(maxHeight: .infinity)
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
