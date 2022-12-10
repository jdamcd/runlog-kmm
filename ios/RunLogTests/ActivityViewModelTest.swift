@testable import Kilometer
import RunLogShared
import XCTest

@MainActor
final class ActivityViewModelTest: XCTestCase {
    var viewModel: ActivityViewModel!
    var mockStrava: StravaMock!

    override func setUp() {
        mockStrava = StravaMock()
        viewModel = ActivityViewModel(strava: mockStrava)
    }

    func testLoadSuccessSetsLoadingThenData() {
        let activity = ActivityDetails.with(id: 1)
        mockStrava.activityDetails = ResultData(data: activity)

        viewModel.load(id: 1)

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data(activity))
    }

    func testLoadFailureSetsLoadingThenError() {
        mockStrava.activityDetails = ResultError(error: KotlinThrowable(), recoverable: true)

        viewModel.load(id: 1)

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .error)
    }

    func testLinkUrlPassesId() {
        XCTAssertEqual(URL(string: "testLinkUrl:123"), viewModel.linkUrl(id: 123))
    }
}
