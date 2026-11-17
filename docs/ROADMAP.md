# Development roadmap

## Milestone 0 — Project foundation
- [x] Forge 1.20.1 / Java 17 Gradle project.
- [x] UUID-keyed player persistence.
- [x] Server-authoritative dialogue packet.
- [x] Observer intro and death reactions.
- [x] Death snapshot persistence.
- [x] Basic progression state and inspection commands.
- [x] CI build passes for the base project.

## Milestone 1 — Echoes of the Past
- [x] Echo entity tied to saved death snapshots.
- [x] Reconstruct armor and held-item appearance.
- [x] Capture player skin profile and model type.
- [x] Owner-only entity tracking.
- [x] Add Guide, Warrior, Wanderer and Traitor personalities.
- [ ] Verify exact skin rendering and spectral effect in a live client.
- [ ] Test reconnect, dimension changes and multiplayer edge cases.

## Milestone 2 — The Observer and memory
- [x] Memory Fragment item and per-player memory counter.
- [x] Connect first memory milestone to divine progression.
- [x] Add initial Observer reactions to memory and power use.
- [ ] Add Old Self / Darkness dialogue branches without revealing the twist too early.
- [ ] Add ancient discovery triggers and dialogue cooldowns.
- [ ] Add per-player story event history.

## Milestone 3 — First divine abilities
- [x] Implement first Space ability: Rift.
- [x] Server-side unlock validation.
- [x] Persistent five-second cooldown.
- [x] Collision and world-border checks for destination.
- [x] Portal particles, teleport sound and Observer reaction.
- [ ] Add configurable ability balance and dedicated client keybind.
- [ ] Implement the next Space ability: Spatial Step.

## Milestone 4 — World and first story chapter
- [ ] Ancient structure generation.
- [ ] First memory chamber and story encounter.
- [ ] First boss with server-authoritative phases.
- [ ] First route into the Other Side dimension.

## Milestone 5 — Full narrative systems
- [ ] Player-specific story flags and branching choices.
- [ ] World anomalies and reactions to restored power.
- [ ] Multiple endings and the Reset route.
- [ ] Dedicated-server and modpack compatibility testing.
