
                             Gource

              software version control visualization

                Copyright (C) 2009 Andrew Caudwell

                  http://gource.googlecode.com/

Contents
========

1. Description
2. Requirements
3. Using Gource
4. Copyright

1. Description
==============

OpenGL-based 3D visualisation tool for source control repositories.

The repository is displayed as a tree where the root of the repository is the
centre, directories are branches and files are leaves. Contributors to the
source code appear and disappear as they contribute to specific files and
directories.

2. Requirements
===============

Gource's display is rendered using OpenGL and requires a 3D accelerated video
card to run.

3. Using Gource
===============

gource <options> <path>

options:

    -f      Fullscreen

    -WIDTHxHEIGHT
            Set the window size. If -f is also supplied, will attempt to set
            the video mode to this also.

    -p, --start-position POSITION
            Begin at some position in the log (between 0.0 and 1.0).

        --stop-position  POSITION
            Stop (exit) at some position in the log (does not work with STDIN).

        --stop-on-idle
            Stop on break in activity. May be combined with --stop-position.

        --stop-at-end
            Stop (exit) at the end of the log / stream.

        --loop
            Loop back to the start of the log when the end is reached.

    -a, --auto-skip-seconds SECONDS
            Skip to next entry if nothing happens for a number of seconds.

    -s, --seconds-per-day SECONDS
            Speed of simulation in seconds per day.

        --realtime
            Realtime playback speed.

    -i, --file-idle-time SECONDS
            Time in seconds files remain idle before they are removed.

    -e, --elasticity FLOAT
            Elasticity of nodes.

    -b, --background FFFFFF
            Background colour in hex.

    --date-format FORMAT
            Specify display date string (strftime format).

    --log-format FORMAT
            Specify format of the log being read (git,cvs,hg,bzr,custom).
            Required when reading STDIN.

    --git-branch
            Get the git log of a branch other than the current one.

    --git-log-command
            Print the git log command used by gource.

    --hg-log-command
            Print the hg log (Mercurial) command used by gource.

    --bzr-log-command
            Print the bzr log (Bazaar) command used by gource.

    --cvs-exp-command
            Print the cvs-exp.pl log command used by gource.

    --follow-user USER
            Have the camera automatically follow a particular user.

    --highlight-user USER
            Highlight the names of a particular user.

    --highlight-all-users
            Highlight the names of all users.

    --file-filter REGEX
            Filter out any files matching a specified regular expression.

    --user-image-dir DIRECTORY
            Directory containing .jpg or .png images of users 
            (eg 'Full Name.png') to use as avatars.

    --default-user-image IMAGE
            Path of .jpg or .png to use as the default user image.

    --colour-images
            Colourize user images.

    --crop AXIS
            Crop view on an axis (vertical,horizontal).

    --multi-sampling
            Enable multi-sampling.

    --bloom-multiplier FLOAT
            Adjust the amount of bloom.

    --bloom-intensity FLOAT
            Adjust the intensity of the bloom.

    --disable-progress
            Disable progress bar.

    --disable-bloom
            Disable bloom effect.

    --max-files
            Set the maximum number of files. Excess files will be discarded.

    --max-file-lag SECONDS
            Max time files of a commit can take to appear.

    --max-user-speed UNITS
            Max speed users can travel per second.

    --user-friction SECONDS
            Time users come to a complete hault.

    --user-scale SCALE
            Change scale of users.

    --camera-mode MODE
            Camera mode (overview,track).

    --hide DISPLAY_ELEMENT
            Hide display element (date,users,files,tree,usernames,filenames,dirnames).

            Separate multiple elements with commas.

    --output-ppm-stream FILE
            Write frames as PPM to a file ('-' for STDOUT).

    --output-framerate FPS
            Framerate of output (used with --output-ppm-stream).

    path    Either a Git, Bazaar or Mercurial directory, a pre-generated log
            file (see log commands or the custom log format) or '-' to read
            STDIN. If path is ommited gource will attempt to read a log from
            the current directory.

Git, Bazaar and Mercurial Examples:

View the log of the respository in the current path:

    gource

View the log of a project in the specified directory:

    gource my-project-dir

Save a copy of the log using in a special log format and play it back
(example is for Git):

    cd my-git-project
    `gource --git-log-command` > my-git-project.log
    gource my-git-project.log

NOTE: --git-log-command assumes you have a recent version git of that supports
      all the options. You may need to modify the command line to work with an
      older version (changing %aN to %an for instance).

Other Version Control Systems:

Visit the Gource homepage for guides and examples of using Gource with other
version control systems:

    CVS       - http://code.google.com/p/gource/wiki/CVS
    SVN       - http://code.google.com/p/gource/wiki/SVN

Custom Log Format:

If you want to use Gource with something other than the supported systems,
there is a pipe ('|') delimited custom log format:

    timestamp - A unix timestamp of when the update occured.
    username  - The name of the user who made the update.
    type      - initial for the update type - (A)dded, (M)odified or (D)eleted.
    file      - Path of the file updated.
    colour    - A colour for the file in hex (FFFFFF) format. Optional.

Recording Videos:

See the guide on the homepage for examples of recording videos with Gource:

    http://code.google.com/p/gource/wiki/Videos

Interface:

The time shown in the top left of the screen is set initially from the first
log entry read and is incremented according to the simulation speed 
(--seconds-per-day).

Pressing SPACE at any time will pause/unpause the simulation. While paused you
may use the mouse to inspect the detail of individual files and users.

TAB cycles through selecting the current visible users.

The camera mode, either tracking activity or showing the entire code tree, can
be toggled using the Middle mouse button.

You can drag the left mouse button to manually control the camera. The right
mouse button rotates the view.

Interactive keyboard commands:

    (V)   Toggle camera mode
    (C)   Displays Gource logo
    (M)   Toggle mouse visibility
    (N)   Jump forward in time to next log entry.
    (+-)  Adjust simulation speed.
    (<>)  Adjust time scale.
    (TAB) Cycle through visible users
    (ESC) Quit

4. Copyright
============

Gource - software version control visualization
Copyright (C) 2009 Andrew Caudwell <acaudwell@gmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
