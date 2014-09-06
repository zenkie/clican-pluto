//
//  Project.h
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Project : NSObject{
    NSNumber *_projectId;
    NSString *_projectName;
    NSArray *_kpis;
}
@property (nonatomic, retain) NSNumber* projectId;
@property (nonatomic, copy) NSString* projectName;
@property (nonatomic, retain) NSArray* kpis;
@end