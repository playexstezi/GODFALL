# Test plan

Run these checks on a disposable world before using the mod in a long-running save.

## Basic startup
1. Build with Java 17 and Gradle 8.8 using gradle --no-daemon build.
2. Start a client with Forge 1.20.1.
3. Start a dedicated Forge server and connect a client.
4. Confirm the Observer's first line appears only for the joining player.

## Memory progression
1. As an operator, run /godfall give_fragment.
2. Use the Memory Fragment from the hotbar.
3. Run /godfall status and confirm the memory count increased and Rift is unlocked.
4. Reconnect and confirm the progression remains saved.
5. Repeat with a second account and confirm its progression is independent.

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
4. Join with a second account and confirm that account cannot see the first player's Echo.
5. Disconnect/reconnect and change dimensions; confirm Echoes are reconstructed without duplicates.

## Known unverified behavior
- Exact custom-skin texture rendering across online/offline profiles.
- Visual quality of the spectral tint and armor layer.
- Multiplayer tracking edge cases under latency.
- Compatibility with large modpacks and other entity/rendering mods.
