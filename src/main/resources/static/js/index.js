// Split view support
var resizeEvent = new Event("paneresize");
Split(['#editor', '#graph'], {
    sizes: [20, 80],
    onDragEnd: function() {
        var svgOutput = document.getElementById("svg_output");
        if (svgOutput != null) {
            svgOutput.dispatchEvent(resizeEvent);
        }
    }
});

function updateSource() {
    var source = document.getElementById("raw").innerText;

    updateGraph(source);
}

var parser = new DOMParser();
var worker;

function updateGraph(source) {
    if (worker) {
        worker.terminate();
    }

    document.getElementById("output").classList.add("working");
    document.getElementById("output").classList.remove("error");

    worker = new Worker("./js/worker/worker.js");

    worker.onmessage = function(e) {
        document.getElementById("output").classList.remove("working");
        document.getElementById("output").classList.remove("error");

        updateOutput(e.data);
    }

    worker.onerror = function(e) {
        document.getElementById("output").classList.remove("working");
        document.getElementById("output").classList.add("error");

        var message = e.message === undefined ? "An error occurred while processing the graph input." : e.message;

        var error = document.querySelector("#error");
        while (error.firstChild) {
            error.removeChild(error.firstChild);
        }
        error.appendChild(document.createTextNode(message));

        console.error(e);
        e.preventDefault();
    }

    var params = { src: source, options: { engine: "dot", format: "svg" } };
    worker.postMessage(params);
}

function clearOutput() {
    var graph = document.getElementById("output");

    var svg = graph.getElementsByTagName("svg")[0];
    if (svg) {
        graph.removeChild(svg);
    }

    var img = graph.getElementsByTagName("img")[0];
    if (img) {
        graph.removeChild(img);
    }

    var textarea = graph.getElementsByTagName("textarea")[0];
    if (textarea) {
        graph.removeChild(textarea);
    }
}

function updateOutput(result) {
    clearOutput();
    var graph = document.getElementById("output");
    var format = document.querySelector("#format select").value;

    if (format == "svg") {
        var svg = parser.parseFromString(result, "image/svg+xml").documentElement;
        svg.id = "svg_output";
        graph.appendChild(svg);

        var panZoom = svgPanZoom(svg, {
            zoomEnabled: true,
            controlIconsEnabled: true,
            fit: true,
            center: true,
            minZoom: 0.1
        });

        svg.addEventListener('paneresize', function(e) {
            panZoom.resize();
        }, false);
        window.addEventListener('resize', function(e) {
            panZoom.resize();
        });
    } else if (format == "png") {
        var image = Viz.svgXmlToPngImageElement(result);
        graph.appendChild(image);
    }
}

document.querySelector("#format select").addEventListener("change", function(e) {
    var source = document.getElementById("raw").innerText;

    if (e.currentTarget.value === "dot") {
        clearOutput();
        var graph = document.getElementById("output");
        var textarea = document.createElement("textarea");
        textarea.value = source;
        graph.appendChild(textarea);
    } else {
        updateGraph(source);
    }
});

updateGraph(document.getElementById("raw").innerText);