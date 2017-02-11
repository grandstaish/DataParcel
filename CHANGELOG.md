Change Log
==========

Version 2.0.0 *(2017-02-11)*
----------------------------

* Remove RegisterAdapter. ProcessorConfig should be used instead.
* Undeprecate PaperParcel.Options.
* Consolidate ProcessorConfig API and PaperParcel.Options as they share many of the same APIs.
* Favours local options over global options.
* More descriptive error messages.
* Allow PaperParcel class inheritance.
* Allow java.lang.Object adapters.
* Fix: Jack compilation.
* Fix: property method matching when variables start with 'is'

Version 2.0.0-beta2 *(2016-12-29)*
----------------------------

* Added `ProcessorConfig` API for adding custom `TypeAdapter`s and configuring other options in
  the processor.
* Deprecated `RegisterAdapter` API in favour of `ProcessorConfig`
* Deprecated `PaperParcel.Options` API in favour of `ProcessorConfig`
* Re-added `Serializable` support
* Performance improvements in the compiler and in the generated code
* Fix: allow use of "new" as a variable name in kotlin (as it isn't a kotlin keyword)
* Validate kapt version and give a helpful error message if the wrong version is being used
* Fix: allow use of contravariant generic types
* Greatly improve type matching system. This allows for adapters to handle complex generic types,
  including intersection types.

Version 2.0.0-beta1 *(2016-11-27)*
----------------------------

 * Change package name from `nz.bradcampbell.paperparcel` to just `paperparcel`
 * `TypeAdapter`s can now list other `TypeAdapter`s or `Class`es as constructor parameters to allow
   for adding proper support for non-standard container and types (and more).
 * Removed "wrapper" types. Each wrapper is replaced with a `Parcelable.Creator` and a `writeToParcel`
   implementation for classes to manually use/call in their model objects.
 * Removed the `TypedParcelable` interface as wrappers no longer exist
 * Removed `PaperParcels` class and all reflection calls
 * Removed Mapping file
 * Removed support for `Serializable` out of the box. Users can opt-in to using `Serializable` via
   explicit `TypeAdapter`s
 * Removed `AccessorName` API
 * Renamed `DefaultAdapter` to `RegisterAdapter` and removed all other types of adapter scoping
   (field and class scopes) as they served no purpose
 * Force annotated classes to implement `Parcelable` as wrappers have been removed
 * Greatly improve usage from Java
 * Add a more powerful abstraction for excluding fields
 * Package paperparcel as an AAR (includes proguard rules)
 * Adapter instances are defined as static constants to reduce the amount of allocations when
   parcelling. All built-in `TypeAdapter`s are singleton instances where they can be.
 * Method count of output greatly reduced (as well as output being cleaner and easier to understand)
 * Improved error messaging
 * Fix: multi module builds
 * Release now on jcenter and maven central instead of jitpack

Version 1.0.1 *(2016-10-31)*
----------------------------

* Fix: Can't reference `CREATOR` on `com.google.android.gms.maps.model.LatLng`

Version 1.0.0 *(2016-05-27)*
----------------------------

* Initial release.
