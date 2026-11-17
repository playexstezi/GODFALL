# GODFALL: The Forgotten Creator

**GODFALL** is a story-driven RPG / psychological-horror mod for **Minecraft 1.20.1 + Forge 47.x**.

You are the Creator, but your memories and powers are gone. The world remembers you differently than you remember yourself. An Observer speaks from somewhere inside your thoughts, while echoes of previous deaths may know more than they should.

## Implemented foundation

- Forge 1.20.1 project setup with Java 17.
- UUID-keyed persistent player progression stored in Overworld SavedData.
- Death snapshots with player identity, dimension, position, rotation, skin profile, armor and held items.
- Echo entities reconstructed from saved death history, with owner-only tracking, player skin, armor, held items and spectral particles.
- Four Echo personalities with distinct Russian dialogue when the owner interacts with them.
- Registered Echo mob attributes for runtime navigation and entity initialization.
- The Observer's first-awakening line, death reactions and memory/ability reactions.
- Memory Fragment item that advances personal memory progression.
- Craftable, single-use Memory Altar: craft it with an Echo Shard, Amethyst Shards and Polished Deepslate, place it and right-click it to receive a Memory Fragment.
- The first divine ability, Rift, unlocked by the first memory fragment.
- Configurable client key mapping for Rift (default: R).
- Server-validated ability packet; the client never chooses its teleport destination.
- Commands: /godfall status, /godfall memories, /godfall rift and operator-only /godfall give_fragment.
- GitHub Actions build workflow.

This is an early playable foundation, not the complete mod. The Echo renderer and multiplayer behavior still need in-game verification. Naturally generated ancient structures, the first boss, the Other Side dimension, deeper story branches, audio and the remaining divine abilities are not implemented yet.

## Build

Requirements: Java 17 JDK, Gradle 8.8 and internet access for the first dependency download.

Run: gradle --no-daemon build

The mod jar will be in build/libs/.

Development runs: gradle runClient and gradle runServer.

Accept the Minecraft EULA in the generated run directory before starting the test server.

## Test commands

- /godfall status — current stage, fragments, deaths and unlocked ability count.
- /godfall memories — restored memory-fragment count and current stage.
- /godfall give_fragment — operator-only test command to receive a Memory Fragment.
- /godfall rift — cast Rift after unlocking it.

### First memory route

1. Find an Echo Shard in an Ancient City.
2. Craft a Memory Altar using 1 Echo Shard, 4 Amethyst Shards and 4 Polished Deepslate.
3. Place the altar and right-click it once. It changes to its activated appearance and gives one Memory Fragment.
4. Use the Memory Fragment from your hand. The first fragment unlocks Rift.
5. Press R by default (changeable in Controls) or use /godfall rift.

The altar is single-use: its activated state is saved with the block and prevents repeated rewards. If the player's inventory is full, the fragment drops nearby.

Rift moves the player up to five blocks forward, searches for a collision-free destination and has a five-second cooldown. Right-click an Echo to hear a line from its personality. The same Echo will not repeat dialogue continuously; it has a short speaking cooldown.

## Design principles

- Server-authoritative gameplay.
- Separate story and progress for every UUID.
- No external AI/API dependency while playing.
- The Observer, the Old Self and the Darkness must not reveal the central truth too early.
- Compatibility claims are made only after real modpack testing.

See docs/ARCHITECTURE.md, docs/ROADMAP.md and docs/TESTING.md.
