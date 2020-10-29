import XCTest

class RunLogUITests: XCTestCase {

    override func setUp() {
        continueAfterFailure = false
    }

    func testLaunchesSuccessfully() {
        XCUIApplication().launch()
    }

    func testLaunchPerformance() {
        if #available(iOS 14.0, *) {
        
            measure(metrics: [XCTApplicationLaunchMetric()]) {
                XCUIApplication().launch()
            }
        }
    }
}
