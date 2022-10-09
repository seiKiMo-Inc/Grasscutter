# Language Files
All language files should be contained here.\
**Language File Path**: `src/main/resources/lang`

# General Format
**All** language files should contain:
- A `details` field
  - Include the name of the language
  - Include any authors who contributed
    - You can simplify to a group as needed
  - Include the language code
    - This is the code that will be used to select the language
    - This should be in the format of `language-country` (e.g. `en-us`)
    - If the language is not specific to a country, use `language` (e.g. `en`)
- Fields from [Language Fields](#language-fields)

# Language Fields
The following fields are required for all language files:
- `system` - Contains system messages.
  - Messages related to **core internal functionality** are sorted here.
- `commands` - Contains messages for commands.
  - These should be broken down in the following order:
  - `command`, `sub-command`, `messages` (fields are as needed)
- `game` - Contains messages for the game.
  - Messages which are **primarily** related to the game-internals are sorted here.

### NOTE: Language fields update as needed. Please check back for updates.