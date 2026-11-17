# Architecture

## Runtime authority

Gameplay state is owned by the logical server. The client receives only the messages and visual state it is allowed to display. This keeps progression server-authoritative and gives multiplayer players separate narratives.

Ability keybinds send intent only. For example, the Rift C2S packet contains no coordinates or target position. The server checks the player's unlock state and cooldown, calculates a destination, validates collision and world-border bounds, and performs the teleport.

## Persistent player state

GodfallSavedData is stored in the Overworld's SavedData storage. It maps player UUIDs to GodfallPlayerData instances, so progress follows a player across dimensions and server restarts.

The initial record stores the intro-seen flag, death counter, death snapshots, memory-fragment count, divine stage, unlocked ability identifiers and Rift cooldown timestamp.

Death snapshots currently record UUID/name, dimension, coordinates, rotation, skin profile texture properties, armor and held items. They are intended as the data source for Echoes of the Past.

## Dialogue networking

The Observer's lines are selected on the server. DialogueS2CPacket sends a line to one player through Forge's SimpleChannel. The client only displays received text; it does not decide story outcomes.

Dialogue is local and deterministic. There is no external model/API dependency.

## Echo implementation

An Echo is linked to a saved death snapshot, not generated as an unrelated mob. Its appearance is reconstructed from that snapshot and uses the player's own skin profile where the client can resolve it. The blue spectral look is a rendering layer, not a replacement for the player's identity.

Echo tracking is restricted to its owner. The Echo renderer and skin-cache behavior still require in-game verification against Forge 47.x on Minecraft 1.20.1. Multiplayer visibility and reconnect behavior also need integration testing.

## Compatibility

The project targets Java 17, Minecraft 1.20.1 and Forge 47.x. No compatibility with large modpacks is claimed until integration testing has been performed.
