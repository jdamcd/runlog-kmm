@testable import Kilometer
import RunLogShared
import XCTest

@MainActor
final class ActivityViewModelTest: XCTestCase {
    var viewModel: ActivityViewModel!
    var mockActivity: ActivityMock!

    override func setUp() {
        mockActivity = ActivityMock()
        viewModel = ActivityViewModel(stravaActivity: mockActivity)
    }

    func testLoadSuccessSetsLoadingThenData() {
        let activity = ActivityDetails.with(id: 1)
        mockActivity.activityDetails = ResultData(data: activity)

        viewModel.load(id: 1)

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data(activity))
    }

    func testLoadFailureSetsLoadingThenError() {
        mockActivity.activityDetails = ResultError(error: KotlinThrowable(), recoverable: true)

        viewModel.load(id: 1)

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .error)
    }

    func testLinkUrlPassesId() {
        XCTAssertEqual(URL(string: "testLinkUrl:123"), viewModel.linkUrl(id: 123))
    }
}
