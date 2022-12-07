import XCTest

class RunLogUITests: XCTestCase {
    override func setUp() {
        continueAfterFailure = false
    }

    func testLaunchesSuccessfully() {
        XCUIApplication().launch()
    }

    func testLaunchPerformance() {
        measure(metrics: [XCTApplicationLaunchMetric()]) {
            XCUIApplication().launch()
        }
    }
}
