# Test plan

Run these checks on a disposable world before using the mod in a long-running save.

## Basic startup
1. Build with Java 17 and Gradle 8.8 using gradle --no-daemon build.
2. Start a client with Forge 1.20.1.
3. Start a dedicated Forge server and connect a client.
4. Confirm the Observer's first line appears only for the joining player.
5. Confirm the Memory Altar appears in the creative inventory and its block/item models load without missing-texture errors.

## Memory Altar and progression
1. In a disposable world, obtain an Echo Shard, four Amethyst Shards and four Polished Deepslate.
2. Craft the Memory Altar and place it.
3. Right-click it once; confirm it changes appearance and gives exactly one Memory Fragment.
4. Right-click the activated altar again; confirm no second fragment is awarded.
5. Break and replace the altar; confirm the new altar can be activated once.
6. Fill the inventory before activation; confirm the fragment drops nearby instead of disappearing.
7. Use the Memory Fragment from the hotbar.
8. Run /godfall status and confirm the memory count increased and Rift is unlocked.
9. Reconnect and confirm the progression remains saved.
10. Repeat with a second account and confirm its progression is independent.

## Rift
1. Press R, or use /godfall rift.
2. Confirm the player moves forward and the portal effects/sound play.
3. Try again immediately and confirm the cooldown is enforced.
4. Change the key in Controls and confirm the new binding works.
5. Test near walls, tight spaces and the world border; the server should reject unsafe destinations.

## Echoes
1. Record a death in a disposable world.
2. Respawn and confirm an Echo appears for the owner.
3. Confirm its armor and held items match the saved death snapshot.
4. Right-click the Echo and confirm it speaks in its assigned personality.
5. Confirm repeated interaction is limited by the short dialogue cooldown.
6. Join with a second account and confirm that account cannot see or interact with the first player's Echo.
7. Disconnect/reconnect and change dimensions; confirm Echoes are reconstructed without duplicates.

## Known unverified behavior
- Memory Altar crafting, blockstate persistence, reward delivery and model rendering in a live client.
- Exact custom-skin texture rendering across online/offline profiles.
- Visual quality of the spectral tint and armor layer.
- Multiplayer tracking edge cases under latency.
- Compatibility with large modpacks and other entity/rendering mods.
