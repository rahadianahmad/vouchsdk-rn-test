//
//  Vouch.swift
//  vouchtest
//
//  Created by Aayush Thapa on 3/11/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation

@objc(Vouch)

class Vouch: NSObject{
  
  private var count = 0
  
  @objc
  func increment(){
    count += 1
    
    print("count is \(count)")
  }
  
  @objc
  func constantsToExport() -> [AnyHashable: Any]! {
    return [
      "number": 12,
      "string": "foo",
      "array": [1,2,3,4],
      "object": ["a": 1, "b": 2],
      "boolean": true
    ]
  }
  
  @objc
  static func requiresMainQueueSetup() -> Bool{
    return true
  }
}


