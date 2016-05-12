//
//  barcodeViewController.h
//  sampleProject
//
//  Created by ASAP Systems International on 5/12/16.
//
//

#import <UIKit/UIKit.h>
#import <Cordova/CDV.h>
@interface barcodeViewController : UIViewController
@property (nonatomic, strong) id <CDVCommandDelegate> commandDelegate;
@property (nonatomic, strong) CDVInvokedUrlCommand* command;

@end
