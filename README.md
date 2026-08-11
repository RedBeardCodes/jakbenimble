# JakBeNimble

Jakarta Application Server Without the Drama

JakBeNimble (JBN) is a lightweight, CDI-native Jakarta application framework for building modular Java applications.

JBN provides a small runtime core and an extension model for adding capabilities such as HTTP, data access, messaging, and other application concerns.  Applications choose the capabilities they need rather than adopting a monolithic application server.

## Goals

- CDI-native - CDI is the programming model at the heart of JBN.
- Modern - capabilities are provided through independent extensions.
- Implementation agnostic - JBN avoids coupling application APIs to a particular implementation where applicable.
- Small footprint - applications should depend only on what they use.
- Fast startup - keep runtime initialization simple and predictable.
- Curated dependencies - dependencies are added deliberately rather than transitively pulling in an entire ecosystem.
- Server-side rendering friendly - JBN is designed for applications that render HTML on the server as well as applications exposing APIs.
- Plain Java - use standard Java and Jakarta APIs rather than inventing another programming model.

## Architecture

JBN separates the application runtime from the capabilities an application uses.

The core provides the runtime and bootstrap infrastructure.  Extensions integrate additional capabilities without requiring the core to depend on those capabilities directly.

Extensions are discovered using Java's built-in `ServiceLoader` mechanism.

This keeps the core small while allowing applications to assemble the runtime they actually need.

## What JakBeNimble is Not

JBN is deliberately **not** a full Jakarta EE application server.

It is not intended to:

- run every Jakarta Specification
- provide every infrastructure service out of the box
- dictate a single implementation for every capability
- become a kitchen-sink application framework
- hide the underlying Java and Jakarta APIs behind another abstraction layer

The goal is not to provide *everything*.

The goal is to provide a clean foundation for the things an application actually needs.

## Project Status

**Early Development**

JBN is currently under active development and APIs may change before the first stable release.

## Modules

The project is organized into small modules rather than a single runtime dependency.

- `jakbenimble-core` - core runtime and application bootstrap
- `jakbenimble-spi` - extension and bootstrap interfaces
- `jakbenimble-web` - HTTP/web integration
- `jakbenimble-testapp` - test application used during development

Additional capabilities will be provided as independent extensions where appropriate.

## Why?

Java application development does not need another framework that tries to solve every problem at once.

JBN is an experiment in building a small, composable Jakarta application runtime that stays close to Java, keeps dependencies under control, and gets out of the application's way.

