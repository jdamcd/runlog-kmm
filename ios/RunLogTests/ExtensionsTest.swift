@testable import Kilometer
import XCTest

class ExtensionsTest: XCTestCase {
    func testExtractsURLParameterByName() {
        let example = URL(string: "http://www.example.com/hello?myparam=abc")!

        let result = example.paramValue("myparam")

        XCTAssertEqual("abc", result)
    }

    func testNilWhenExtractingMissingURLParameter() {
        let example = URL(string: "http://www.example.com/hello?myparam=abc")!

        let result = example.paramValue("nope")

        XCTAssertNil(result)
    }
}
