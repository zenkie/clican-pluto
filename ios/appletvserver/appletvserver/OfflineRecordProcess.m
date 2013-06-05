//
//  DBProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-5-30.
//
//

#import "OfflineRecordProcess.h"
#import "SBJSON.h"
@implementation OfflineRecordProcess
@synthesize database = _database;
@synthesize offlineRecords = _offlineRecords;
-(id) init{
    self = [super init];
    if(self){
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *documentsDirectory = [paths objectAtIndex:0];
        self.database = [documentsDirectory stringByAppendingPathComponent:@"uranus"];
         NSString* path = [self.database stringByAppendingPathComponent:@"OfflineRecord.json"];
        self.offlineRecords = [NSMutableDictionary dictionary];
        if([[NSFileManager defaultManager] fileExistsAtPath:path]){
            NSString* content = [NSString stringWithContentsOfURL:[NSURL fileURLWithPath:path] encoding:NSUTF8StringEncoding error:nil];
            if(content!=NULL&&content.length>0){
                self.offlineRecords = [self jsonToOfflineRecords:content];
            }
        }else{
            if(![[NSFileManager defaultManager] fileExistsAtPath:self.database]){
                [[NSFileManager defaultManager] createDirectoryAtPath:self.database withIntermediateDirectories:YES attributes:nil error:nil];
            }
        }
    }
    return self;
}
-(void)dealloc{
    [super dealloc];
    TT_RELEASE_SAFELY(_database);
    TT_RELEASE_SAFELY(_offlineRecords);
}
-(NSMutableDictionary*) jsonToOfflineRecords:(NSString*) json{
    NSDictionary* dict = [json JSONValue];
    NSMutableDictionary* records = [NSMutableDictionary dictionary];
    for(NSString* url in dict.allKeys){
        NSDictionary* value = [dict valueForKey:url];
        OfflineRecord* record = [[[OfflineRecord alloc] init] autorelease];
        record.fileName = [value valueForKey:@"fileName"];
        record.filePath = [value valueForKey:@"filePath"];
        record.fileSize = [value valueForKey:@"fileSize"];
        record.displayName = [value valueForKey:@"displayName"];
        record.url = [value valueForKey:@"url"];
        record.fileType = [[value valueForKey:@"fileType"] integerValue];
        [records setValue:record forKey:url];
    }
    return records;
}

-(NSData*) offlineRecordsToJson:(NSDictionary*) offlineRecords{
    if(offlineRecords.count==0){
        return [NSData data];
    }
    NSMutableArray* array = [NSMutableArray array];
    for (NSString* key in offlineRecords.allKeys){
        OfflineRecord* offlineRecord = [offlineRecords objectForKey:key];
        NSString* record = [NSString stringWithFormat:@"\"%@\":{\"url\":\"%@\",\"fileName\":\"%@\",\"displayName\":\"%@\",\"filePath\":\"%@\",\"fileType\":\"%@\",\"fileSize\":%ld}",offlineRecord.url,offlineRecord.url,offlineRecord.fileName,offlineRecord.displayName,offlineRecord.filePath,offlineRecord.fileType,offlineRecord.fileSize];
        [array addObject:record];
    }
    NSString* result = [NSString stringWithFormat:@"{%@}",[array componentsJoinedByString:@","]];
    return [result dataUsingEncoding:NSUTF8StringEncoding];
}
-(void) insertOrUpdateOffileRecord:(OfflineRecord*) offlineRecord{
    NSString* path = [self.database stringByAppendingPathComponent:@"OfflineRecord.json"];
    NSMutableDictionary* records = [NSMutableDictionary dictionary];
    if([[NSFileManager defaultManager] fileExistsAtPath:path]){
        NSString* content = [NSString stringWithContentsOfURL:[NSURL fileURLWithPath:path] encoding:NSUTF8StringEncoding error:nil];
        if(content!=NULL&&content.length>0){
            records = [self jsonToOfflineRecords:content];
        }
    }
    [records setValue:offlineRecord forKey:offlineRecord.url];
    [self.offlineRecords setValue:offlineRecord forKey:offlineRecord.url];
    NSData *jsonData = [self offlineRecordsToJson:records];
    [jsonData writeToFile:path atomically:YES];
}

-(void) deleteOffileRecord:(OfflineRecord*) offlineRecord{
    NSString* path = [self.database stringByAppendingPathComponent:@"OfflineRecord.json"];
    NSMutableDictionary* records = [NSMutableDictionary dictionary];
    if([[NSFileManager defaultManager] fileExistsAtPath:path]){
        NSString* content = [NSString stringWithContentsOfURL:[NSURL fileURLWithPath:path] encoding:NSUTF8StringEncoding error:nil];
        if(content!=NULL&&content.length>0){
            records = [self jsonToOfflineRecords:content];
        }
    }
    [records removeObjectForKey:offlineRecord.url];
    [self.offlineRecords removeObjectForKey:offlineRecord.url];
    NSData *jsonData = [self offlineRecordsToJson:records];
    [jsonData writeToFile:path atomically:YES];
}

-(NSArray*) getAllOfflineRecord{
    return [self.offlineRecords allValues];
}
@end