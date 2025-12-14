import SwiftUI

extension String {
    func toColor() -> Color? {
        guard self.count == 6 else { return nil }
        
        let unHexxed = {
            let chars = self.map { $0 }
            
            return stride(from: 0, to: 6, by: 2)
                .map {
                    Double(UInt8(String(chars[$0]) + String(chars[$0 + 1]), radix: 16) ?? 0) / 255.0
                }
        }()
        
        let (red, green, blue) = (unHexxed[0], unHexxed[1], unHexxed[2])
        
        return Color(.sRGB ,red: red, green: green, blue: blue, opacity: 1)
    }
}
