# v2.3.0
## Fixed
- adding installments functionality to the MLB seller.
- furyDoc on installments functionality for MLB seller.

## Fixed
- solution to the exception when returning from the payment flow with installments.

# v2.2.1
## Fixed
- Exception mapping when printing image by bitmap.
- Added signature keystore configuration for release build.

# v2.2.0
## Added
- Beat implementation the new way of tracking flow events.
- Remove the Timber library and replace the event log with the new implementation.
- Added smartPos version number in app info.
- Change name parameter SmartDevice.
- Removed coroutines dependency in the printer functionalities.

# v2.1.0
## Added
- MP Generic Response has been implemented in the following features: Payment Flow, Payment Methods, Smart Information, Printer, Camera Scanner.
- Added flavor to support demo app examples for natives MainApps & MiniApps.
- hide unnecessary functionality for mini apps flavors

## Fixed
- Fixed error related to sdk version no available into smart information option.

# v2.0.0
## Modified
- Update kotlin version

## Fixed
- Fixed on click listener in payment button

## Added
- Coroutines have been implemented in the content resolvers and other layers, such as use cases and the implementations of the features available in the SDK.
- Used Smart App content provider to update hardware buttons state
- Implemented the feature that allows to obtain the SDK and device information.
- Backward compatibility in callbacks when building the paymentFlow deeplink in the SDK.
- Implement generic result to handler sdk answer
- Error handling when inserting a non-numeric amount format.

# v1.3.0
## Added
- Launch payment flow with payment method like parameter
- Launch QR Scanner functionality
## Fixed
- Fixed animation when paring or unpair devices
- Error with successfully approved payment.

# v1.2.0
## Added
- Add ticket printing functionality in bitmap format.
- Added maximum and minimum amount validation functionality.

# v1.1.0
## Added
- Add Refunds example using the public MP API
- add observer connect manager bluetooth
- Added bluetooth printer example screen

# v1.0.0
## Added
- Added Integration SDK v1.0.0
- Added support of bluetooth tools & Bluetooth UI settings.
- Added Home activity and color scheme
- Removed all mercado libre plugins an dependecies
- Implemented payment flow
- Initial template
