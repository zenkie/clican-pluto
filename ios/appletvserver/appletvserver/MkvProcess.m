//
//  MkvProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-4-5.
//
//

#import "MkvProcess.h"
#import "AppDelegate.h"
#import "ffmpeg.h"
#import "AtvUtil.h"

@implementation MkvProcess

@synthesize mkvUrl = _mkvUrl;
@synthesize running = _running;
@synthesize mkvM3u8Content = _mkvM3u8Content;
-(void) dealloc{
    TT_RELEASE_SAFELY(_mkvUrl);
    [super dealloc];
}

-(void) convertToM3u8MkvUrl:(NSString*) url{
    if(self.mkvUrl==nil||![self.mkvUrl isEqualToString:url]||true){
        self.mkvUrl = url;
        NSString* inpath =url;
        if([self.mkvUrl rangeOfString:@"smb://"].location!=NSNotFound){
            inpath = [NSString stringWithFormat:@"http://%@:8081/appletv/noctl/proxy/smb.mkv?url=%@",AppDele.ipAddress,[AtvUtil encodeURL:url]];
        }
        NSString *outpath = AppDele.localMkvM3u8PathPrefix;
        
        if(AppDele.simulate){
            inpath=@"/Users/zhangwei/Desktop/2.mkv";
        }
        inpath = @"/Users/zhangwei/Desktop/2.mkv";
        NSLog(@"OutPath:%@",outpath);
        NSLog(@"InPath:%@",inpath);
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            transfer_code_interrupt = 1;
            @synchronized(AppDele.mkvProcess) {
                if([[NSFileManager defaultManager] fileExistsAtPath:outpath]){
                    [[NSFileManager defaultManager] removeItemAtPath:outpath error:nil];
                }
                [[NSFileManager defaultManager] createDirectoryAtPath:outpath withIntermediateDirectories:YES attributes:nil error:nil];
                NSDate* start = [NSDate date];
                @try {
                    transfer_code_interrupt = 0;
                    NSString* cookie = nil;
                    if([inpath rangeOfString:@"http://"].location!=NSNotFound){
                        cookie=@"Cookie:gdriveid=08D39F59B366F371195050D992B72FD2;\r\n";
                    }
                    
                    NSLog(@"m3u8Path:%@",[outpath stringByAppendingString:@"mkv.m3u8"]);
                    convert_avi_to_m3u8([inpath cStringUsingEncoding:NSASCIIStringEncoding],[[outpath stringByAppendingString:@"mkv.m3u8"] cStringUsingEncoding:NSASCIIStringEncoding],
                                        [[outpath stringByAppendingString:@"%04d.ts"] cStringUsingEncoding:NSASCIIStringEncoding],[cookie cStringUsingEncoding:NSASCIIStringEncoding]);
                    
                }
                @catch (NSException *exception) {
                    NSLog(@"error occured for mkv to m3u8 %@",exception);
                }
                @finally {
                    NSLog(@"Start Date:%@",[start description]);
                    NSLog(@"End Date:%@",[[NSDate date] description]);
                }
            }
        });
    }
}

@end
