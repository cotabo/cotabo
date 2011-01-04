<html>
    <head>
        <title>Welcome to TaskBoard</title>
        <meta name="layout" content="main" />
        <style type="text/css" media="screen">

        .homePagePanel * {
            margin:0px;
        }
        .homePagePanel .panelBody ul {
            list-style-type:none;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody h1 {
            text-transform:uppercase;
            font-size:1.1em;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody {
            background: url(images/leftnav_midstretch.png) repeat-y top;
            margin:0px;
            padding:15px;
        }
        .homePagePanel .panelBtm {
            background: url(images/leftnav_btm.png) no-repeat top;
            height:20px;
            margin:0px;
        }

        .homePagePanel .panelTop {
            background: url(images/leftnav_top.png) no-repeat top;
            height:11px;
            margin:0px;
        }
        h2 {
            margin-top:15px;
            margin-bottom:15px;
            font-size:1.2em;
        }
        </style>
    </head>
    <body>
        <div>
            <h1>Welcome to TaskBoard</h1>
            <p>Congratulations, you are on the best way to improve the management of your Tasks. This site
            will help you structuring all your tasks. It provides something similar to an electronic whiteboard 
            that you can use to spread task across dirtributed teams.
            All this stuff is inspired by Kanban - which is an "agile"</p>
            <p>You can create multiple Boards for multiple projects or workstreams.
            You will be surprised but the amount of data that can be pulled out of the usage of this tool in order to 
            measure your throughput.</p>

            <div id="controllerList" class="dialog">
                <h2>Available Controllers:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                        <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
                    </g:each>
                </ul>
            </div>
        </div>
    </body>
</html>
