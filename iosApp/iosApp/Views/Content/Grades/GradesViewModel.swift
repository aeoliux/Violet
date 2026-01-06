import Shared
import SwiftUI

extension GradesView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var subjects = [String]()
        var subjectsListTask: Task<(), Never>?
        
        var averages = [[(String, SwiftUI.Color, String)]]()
        var averagesTask: Task<(), Never>?
        
        let repos = RepositoryHelper()
        
        override init() {
            super.init()
            
            self.subjectsListTask = Task {
                for await subjects in self.repos.gradesRepository.getSubjectsListFlow() {
                    self.subjects = subjects
                }
            }
            
            self.averagesTask = Task {
                for await averages in self.repos.gradesRepository.getGeneralAveragesFlow() {
                    self.averages = ["Yearly", "1st semester", "2nd semester"]
                        .enumerated()
                        .map { index, label in
                            ["final", "Proposal"]
                                .enumerated()
                                .compactMap { offset, sublabel in
                                    let index = (index * 2) + offset
                                    guard let average = averages.get(index: Int32(index)) as? Double else { return nil }
                                    
                                    return ("\(offset == 0 ? label + " " : "")\(sublabel)", average.averageToColor(), String(format: "%.2f", average))
                                }
                        }
                        .filter { $0[0].2 != "0.00" || $0[1].2 != "0.00" }
                }
            }
        }
        
        deinit {
            self.subjectsListTask?.cancel()
            self.averagesTask?.cancel()
        }
        
        func refresh() async {
            _ = try? await self.repos.gradesRepository.refresh()
        }
    }
}

extension Double {
    func averageToColor() -> SwiftUI.Color {
        return switch self {
        case 5.75...:
            Color.pink
        case 4.75..<5.75:
            Color.cyan
        case 3.75..<4.75:
            Color.blue
        case 2.75..<3.75:
            Color.green
        case 1.75..<2.75:
            Color.yellow
        case 0.75..<1.75:
            Color.red
        case 0.00..<0.75:
            Color.gray
        default:
            Color.black
        }
    }
}
