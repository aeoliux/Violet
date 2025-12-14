import Shared
import SwiftUI

struct HomeGradesSection: View {
    let grades: [(Grade_, SwiftUI.Color)]
    
    var body: some View {
        Section("Latest \(self.grades.count) grades") {
            HStack {
                Spacer()
                
                ForEach(self.grades, id: \.0.id) { (grade, color) in
                    Rectangle()
                        .fill(color)
                        .frame(width: 50, height: 50)
                        .clipShape(RoundedRectangle(cornerRadius: 5))
                        .overlay {
                            Text("\(grade.grade)")
                                .foregroundStyle(Color.black)
                        }
                    
                    Spacer()
                }
            }
        }
    }
}
