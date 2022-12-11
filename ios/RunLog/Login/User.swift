import Foundation
import RunLogShared

class User {
    static let sharedInstance = PersistingUserState()
    private init() {}
}
