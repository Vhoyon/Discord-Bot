[![Build Status](https://travis-ci.org/Vhoyon/Discord-Bot.svg?branch=dev)](https://travis-ci.org/Vhoyon/Discord-Bot)

# Vhoyon's Discord Bot

In this repository we host the source code of our Discord Bot, based of [DV8FromTheWorld's JDA library](https://github.com/DV8FromTheWorld/JDA). This bot is made to be self-hosted and has no particular goal - we do it to learn more about Discord and have fun while coding it!

Currently, this repo also holds the code for the framwork we are building to facilitate the creation of bots, which we will move in another repository shortly.

## Getting Started

These instructions will get you a copy of our project up and running on your local machine for you to self-host your own fork of this bot.

### Prerequisites

You will require Maven and Java >1.8 for this bot to work.

Maven could be installed through most Java IDE : your mileage may vary.

### Installing

You can clone this repo and import the project in any Java IDE that you may want to use. We created this project in Eclipse but we are now using IntelliJ IDEA - as long as you can run a Maven project, you are good to go.

Make sure that you have imported the plugins required via Maven (look up how to do that for your own environment) and that your IDE recognize the root folder `framework`, `res` and `src` as Source folders.

If you have trouble getting the bot up and running, feel free to [create an issue](https://github.com/Vhoyon/Discord-Bot/issues).

### Setting up your environment (Bot Token)

Since this is a self-hosted solution, you will need to provide your own Bot Token to get your bot up and running.

If you don't know how to get your own bot token, you can follow [these instructions](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token) to create yourself one.

Once you have your bot token, you can run the program for the first time. It will detect if there is a `.env` file near the java executable path and if non-existant, will ask and create it for you. All you have to do now is open the `.env` file that was just created, and enter your Bot Token at the required place.

Your `.env` file should look like this :

```properties
# This is the place where you store the environment variables for the bot.
# This includes the bot's TOKEN, its name if you want, etc.

DEBUG=true

CLIENT_ID=
BOT_TOKEN=     # <-- This is where your Bot Token should be pasted to!
```

Now, you can run the bot again, which will detect the `.env` file and wait for your input to bring the bot to life.

Once started, if you followed [these instructions](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token) correctly (the link provided to make your bot join a server will also be outputted to the Bot's console if you added the `CLIENT_ID` value to the `.env` file!), you should see the Bot's status to come online. If you didn't change anything in the code yet, you should be able to send the command `!!help` in a Text Channel and see a list of the available commands!

## Built With

- [JDA](https://github.com/DV8FromTheWorld/JDA) - The Java Discord APIs to allow a bot to run in Java
- [Maven](https://maven.apache.org/) - Dependency Management
- [FastClasspathScanner](https://github.com/lukehutch/fast-classpath-scanner) - Used to find Commands without declaring them

## GitFlow

We use a slightly modified [GitFlow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) methodology, where our modifications types are used when creating branches from the `dev` branch. For example, creating a `README.md` file means creating a branch from `dev` named `tasks/README`.

## ZenHub

We use [ZenHub](https://www.zenhub.com/) to easily manage our [issues](https://github.com/Vhoyon/Discord-Bot/issues). If you haven't tried it yet, we highly recommend it, inline GitHub integrations for issues management is really useful!

## Authors

- Stephano Mehawej ([StephanoMehawej](https://github.com/StephanoMehawej)) - Original Creator and Owner
- Guillaume Marcoux ([V-ed](https://github.com/V-ed)) - Framework maintainer and Owner

See also the list of [contributors](https://github.com/Vhoyon/Discord-Bot/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details