# GODFALL: The Forgotten Creator

**GODFALL** is a story-driven RPG / psychological-horror mod for **Minecraft 1.20.1 + Forge 47.x**.

You are the Creator, but your memories and powers are gone. The world remembers you differently than you remember yourself. An Observer speaks from somewhere inside your thoughts, while echoes of previous deaths may know more than they should.

## Project status

The repository now contains the first development foundation:

- Forge 1.20.1 project setup with Java 17.
- Per-player persistent data keyed by UUID in Overworld SavedData.
- Death-history snapshots: identity, dimension, position, rotation, armor and held items.
- A server-to-client packet for private dialogue.
- The Observer's first-awakening line and contextual death dialogue.
- Initial progression state for memory fragments, divine stages and unlocked ability IDs.
- /godfall status and /godfall memories commands.
- GitHub Actions build workflow.

This is a foundation, **not yet the complete mod**. Ghost entities, exact skin rendering, abilities, structures, bosses, dimensions, audio and the full story are still planned. Stored death snapshots are groundwork for Echoes; they are not yet rendered as ghost companions.

## Build

Requirements: Java 17 JDK, Gradle 8.8 and internet access for the first dependency download.

```bash
gradle --no-daemon build
```

The mod jar will be in build/libs/.

Development runs:

```bash
gradle runClient
gradle runServer
```

Accept the Minecraft EULA in the generated run directory before starting the test server.

## Commands

- /godfall status — current stage, memory fragments and recorded deaths.
- /godfall memories — restored memory-fragment count.

## Design principles

- Server-authoritative gameplay.
- Separate story and progress for every UUID.
- No external AI/API dependency while playing.
- The Observer, the Old Self and the Darkness must not reveal the central truth too early.
- Compatibility claims are made only after real modpack testing.

See docs/ARCHITECTURE.md and docs/ROADMAP.md.
