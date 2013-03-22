//
//  WebContentSync.m
//  appletvserver
//
//  Created by zhang wei on 13-3-22.
//
//

#import "WebContentSync.h"
#import "Constants.h"
#import "ASIHTTPRequest.h"
#import "ZipArchive.h"

@implementation WebContentSync

-(void) syncWebContent{
    
    ASIHTTPRequest *verreq = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:[WEB_CONTENT_SYNC_VERSION_API stringByAppendingFormat:@"?t=%f",[[NSDate new] timeIntervalSince1970]]]];
    [verreq setShouldContinueWhenAppEntersBackground:YES];
    [verreq startSynchronous];
     NSString* version = [[verreq responseHeaders] valueForKey:@"version"];
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* currentVersion = [defaults objectForKey:@"version"];
    if(currentVersion==NULL||![currentVersion isEqualToString:version]){
         NSLog(@"Current version is %@, there is new version %@ to update",currentVersion,version);
        ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:[WEB_CONTENT_SYNC_API stringByAppendingFormat:@"?t=%f",[[NSDate new] timeIntervalSince1970]]]];
        [req setShouldContinueWhenAppEntersBackground:YES];
        [req startSynchronous];
        NSError *error = [req error];
        if (!error) {
            NSData* contentData = [req responseData];
            NSString* filePath = [NSTemporaryDirectory() stringByAppendingString:@"/sync.zip"];
            [[NSFileManager defaultManager] createFileAtPath:filePath contents:contentData attributes:nil];
            ZipArchive *zipArchive = [[ZipArchive alloc] init];
            [zipArchive UnzipOpenFile:filePath];
            NSString *webPath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:@"web/appletv/"];
            [[NSFileManager defaultManager] removeItemAtPath:webPath error:nil];
            [[NSFileManager defaultManager] createDirectoryAtPath:webPath withIntermediateDirectories:YES attributes:nil error:nil];
            [zipArchive UnzipFileTo:webPath overWrite:YES];
            [zipArchive UnzipCloseFile];
            [defaults setValue:version forKey:@"version"];
        }
    }else{
        NSLog(@"Current version is %@, there is no need to update new version %@",currentVersion,version);
    }
}
@end
