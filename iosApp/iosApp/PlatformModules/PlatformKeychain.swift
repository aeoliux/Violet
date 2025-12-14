import Foundation
import Security

public class PlatformKeychain {
    static func savePassFunc(_ password: String) {
        guard let password = password.data(using: .utf8) else {
            return
        }
        
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword as String,
            kSecAttrAccount as String: "synergiaCredentials",
            kSecValueData as String: password
        ]
        
        SecItemAdd(query as CFDictionary, nil)
    }
    
    static func getPassFunc() -> String? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword as String,
            kSecAttrAccount as String: "synergiaCredentials",
            kSecMatchLimit as String: kSecMatchLimitOne,
            kSecReturnAttributes as String: true,
            kSecReturnData as String: true,
        ]
        
        var res: CFTypeRef?
        
        guard
            SecItemCopyMatching(query as CFDictionary, &res) == errSecSuccess,
            let passwordData = (res as? [String: Any])?[kSecValueData as String] as? Data
        else {
            return nil
        }
        
        let password = String(decoding: passwordData, as: UTF8.self)
        return password
    }
    
    static func deletePassFunc() {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword as String,
            kSecAttrAccount as String: "synergiaCredentials",
            kSecMatchLimit as String: kSecMatchLimitOne,
            kSecReturnAttributes as String: true,
            kSecReturnData as String: true,
        ]
        
        SecItemDelete(query as CFDictionary)
    }
}
