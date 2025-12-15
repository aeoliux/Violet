import Shared
import SwiftUI

struct HomeGradesSection: View {
    let grades: [(Grade_, SwiftUI.Color)]
    let onSelect: (_ grade: Grade_) -> Void
    
    var body: some View {
        Section("Latest \(self.grades.count) grades") {
            HStack {
                Spacer()
                
                ForEach(self.grades, id: \.0.id) { (grade, color) in
                    Button {
                        onSelect(grade)
                    } label: {
                        Rectangle()
                            .fill(color)
                            .frame(width: 50, height: 50)
                            .clipShape(RoundedRectangle(cornerRadius: 5))
                            .overlay {
                                Text("\(grade.grade)")
                                    .foregroundStyle(Color.black)
                            }
                    }
                    .buttonStyle(.plain)
                        
                    Spacer()
                }
            }
        }
    }
}
