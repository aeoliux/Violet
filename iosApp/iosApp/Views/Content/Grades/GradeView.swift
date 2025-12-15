import Shared
import SwiftUI

struct GradeView: View {
    let tabs: [(String, String, String)]
    let comment: String?
    
    init(_ grade: Grade_) {
        self.tabs = [
            ("star.fill", "Grade", grade.grade),
            ("person.fill", "Added by", grade.addedBy),
            ("folder.fill", "Category", grade.category),
            ("number", "Value", String(format: "%.2f x %d", grade.gradeValue, grade.weight)),
            ("calendar.badge.plus", "Added at", datetimeFormat.string(from: grade.addDate.toNSDate()))
        ]
        
        self.comment = grade.comment
    }
    
    var body: some View {
        List {
            ForEach(self.tabs, id: \.1) { (icon, label, value) in
                HStack {
                    Image(systemName: icon)
                        .frame(width: 30)
                    Text(label)
                    
                    Spacer()
                    
                    Text(value)
                }
            }
            
            if let comment = self.comment {
                VStack(alignment: .leading) {
                    HStack {
                        Image(systemName: "square.and.pencil")
                            .frame(width: 30)
                        Text("Comment")
                    }
                    
                    Text(comment)
                        .padding()
                }
            }
        }
    }
}

let datetimeFormat = {
    let f = DateFormatter()
    f.dateFormat = "dd MMMM, HH:mm"
    return f
}()
