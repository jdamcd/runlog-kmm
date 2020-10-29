import XCTest
@testable import RunLog

class ExtensionTests: XCTestCase {

    func testExtractsURLParameterByName() {
        let example = URL(string: "http://www.example.com/hello?myparam=abc")!
        
        let result = example.paramValue("myparam")
        
        XCTAssertEqual("abc", result)
    }
    
    func testNilWhenExtractingMissingURLParameter() {
        let example = URL(string: "http://www.example.com/hello?myparam=abc")!
        
        let result = example.paramValue("nope")
        
        XCTAssertEqual(nil, result)
    }

}
