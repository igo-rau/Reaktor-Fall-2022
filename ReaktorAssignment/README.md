## Solution for the REAKTOR [Assignment](https://www.reaktor.com/assignment-fall-2022-developers/) 
(assignment description below)

Assignment is written from the scratch without any libraries and tools - both parsing part and export-to-html-document part. 
As no libraries are used, this version is offline version (generates html in local folder). 

Package name  (String) is considered to be unique and is used as a key. 

Package is presented as an object with properties of name, description and two sets of Strings (one for dependencies and one for reversed dependencies), referencing the names of other packages. 

Packages are stored in the map with package name used as a key and Package-object as a value. Therefore package name is stored two times, but the object itself contains information about package name (in case object needs to be passed somewhere in the future). In future versions either "name"-property can be removed, or some other key can be chosen. 

File poetry.lock is initially copied into ArrayList of Strings to ensure direct access if needed. 

Names, decriptions and dependencies are parsed from the file. Reversed dependencies are built based on existing dependencies. 

Data is exported into single-page html file with alphabetical list/index of installed packages and separate sections for each package. 
Package names in index are clickable, and each package name is equipped with link to the top of the page. 
Each package's section includes information about package's name, description, and alphabetical list of dependencies / reversed dependencies. 
If dependency is installed (checked from map keys), it's name is clickable. 

<h3>Assumptions</h3>
package names are unique and can be used as keys; file poetry.lock is of correct structure; 

<h3>Assignment text</h3>

Some Python projects use Poetry to manage dependencies. Poetry uses a file called poetry.lock to record which packages a project needs and which dependencies those packages have. Here is an example of such a file.

Write a small program in your language of choice that accepts a poetry.lock file as input and exposes some key information about the packages via an HTML user interface. The program should provide the following features:

    The index page lists installed packages in alphabetical order with package names as links
    When following each link, you arrive at information about a single package. The following information should be included:
        Name
        Description
        The names of the packages the current package depends on (i.e. dependencies)
        The names of the packages that depend on the current package (i.e. reverse dependencies)
        The dependencies and reverse dependencies should be clickable and the user can navigate the package structure by clicking from package to package
        Include all optional dependencies as well, but make clickable only those that are installed

Some things to keep in mind:

    We appreciate solutions that have a clear structure and demonstrate your abilities to write clean code that has a good separation of concerns. Think about how other developers might take over the project after you’re done. Also think about how many 3rd party libraries you use. Pick just the amount that you think is suitable for the problem.
    We also appreciate solutions that have a good UX. However, we don’t expect you to come up with a stunning visual appearance. We’re more interested in usability and performance
    We would like to see you implement the parsing part of the assignment from scratch. poetry.lock is a TOML file, but please, do not use any ready-made 3rd party parsers in your solution. At the same time, it is enough to only parse what is needed for the assignment
    Make the following simplifications:
        Ignore package version numbers
        Ignore the dev dependency flag, i.e. consider dev dependencies to be just regular dependencies
        Focus only on the three following sections of the file: package, package.dependencies, package.extras

    Optional dependencies can be found under packages.extras or under packages.dependencies with the flag optional set to true. Look into both places!
    A good sample input file is the poetry.lock of Poetry itself. It can be found here.
    poetry.lock has a versioned format. Make your solution work on version 1.1 which is the version of the sample file linked in this brief as can be checked in the metadata section of the file.

Practicalities:

    Your solution must be hosted publicly, for example in Heroku. This will allow us to review it easily. Provide us with the link when submitting your solution
    Upload the source code to GitHub or similar, preferably as a public repository. Provide us with the link when submitting your solution

Good luck and have fun!
