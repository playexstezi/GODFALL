# Architecture

## Runtime authority

Gameplay state is owned by the logical server. The client receives only the messages and visual state it is allowed to display. This keeps progression server-authoritative and gives multiplayer players separate narratives.

## Persistent player state

GodfallSavedData is stored in the Overworld's SavedData storage. It maps player UUIDs to GodfallPlayerData instances, so progress follows a player across dimensions and server restarts.

The initial record stores the intro-seen flag, death counter, all recorded death snapshots, memory-fragment count, divine stage and unlocked ability identifiers.

Death snapshots currently record UUID/name, dimension, coordinates, rotation, armor and held items. They are intended as the data source for Echoes of the Past.

## Dialogue networking

The Observer's lines are selected on the server. DialogueS2CPacket sends a line to one player through Forge's SimpleChannel. The client only displays received text; it does not decide story outcomes.

Dialogue is local and deterministic. There is no external model/API dependency.

## Planned Echo implementation

An Echo must be linked to one specific death snapshot, not generated as an unrelated mob. Its appearance should be reconstructed from that snapshot and use the player's own skin/profile where the client can resolve it. The blue spectral look is a rendering layer, not a replacement for the player's identity.

The renderer and skin-cache behavior must be verified against Forge 47.x on Minecraft 1.20.1 before claiming exact skin fidelity. Multiplayer visibility must be designed so private Echoes are not accidentally exposed to everyone.

## Compatibility

The project targets Java 17, Minecraft 1.20.1 and Forge 47.x. No compatibility with large modpacks is claimed until integration testing has been performed.
