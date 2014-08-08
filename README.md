PPINOT
======

A key aspect in any process-oriented organisation is the measurement of process performance for the achievement of its
strategic and operational goals. Process Performance Indicators (PPIs) are a key asset to carry out this evaluation,
and, therefore, the management of these PPIs throughout the whole BP lifecycle is crucial.

PPINOT is a set of libraries aimed at facilitating and automating the PPI management. The support includes their
definition using either a graphical or a template-based textual notation, their automated analysis at design-time, and
their automated computation based on processing an event log obtained from a process simulator or a Business Process
Management System.

PPINOT has been integrated into [PRspectives](http://github.com/isa-group/prspectives), which is a multi-perspective
business process modeler.

Components
----------

In its current version, PPINOT is composed of four components: `ppinot-model`, `ppinot-ontology`, `ppinot-oryx` and
`ppinot-templates-angular`. The other two components (`ppinot-xml-owl` and `ppinot-templates`) are obsolete and need
to be updated to the current version of `ppinot-model`. Next, we detail each of them.

### ppinot-model ###

This library provides a Java implementation of the PPINOT metamodel, which is detailed in http://www.isa.us.es/ppinot.
This metamodel provides a foundation on which an automated support for these activities can be built. It identifies the
concepts that are necessary for defining Process Performance Indicators (PPIs) such as the different types of measures
that can be used to compute the PPI value. It was defined to address the challenge of providing PPI definitions that
are unambiguous and complete, traceable to the business process elements used in their definition, independent of the
language used to model business processes (BP) and amenable to automated analysis.

A PPINOT model can be serialized in two different formats:
* As an XML document that can be embedded into a standard BPMN 2.0 XML file. This library provides the serialization and
deserialization mechanisms as well as the XML Schema of the PPINOT model.
* As a JSON file that can be used while implementing REST APIs. `ppinot-model` relies on Jackson to serialize and
deserialize from JSON to Java classes.

Finally, `ppinot-model` also include the computation of the PPIs based on an event log. The current implementation
supports logs in MXML format. However, support to other formats can be easily integrated.

### ppinot-ontology ###

PPINOT ontology is an OWL ontology based on the PPINOT metamodel. It has been developed to enable design-time analysis
of PPIs defined with PPINOT using off-the-shelf OWL Reasoners. It also includes an use case showing how it can be used.

### ppinot-oryx ###

This library is an implementation of Visual PPINOT, which is a graphical notation intended to be used together with
BPMN diagrams. Visual PPINOT has been implemented as an [Oryx](http://bpt.hpi.uni-potsdam.de/Oryx) stencil set that
extends the Oryx-native BPMN stencil set with the symbols of Visual PPINOT. A complete description of these symbols can
be found at <http://www.isa.us.es/ppinot>.

The library includes:
* The stencil set, which include the symbols used in Visual PPINOT in SVG format and a description of the properties and
the connection rules.
* Java code to convert from the PPINOT model to an Oryx diagram and from an Oryx diagram to PPINOT XML.

### ppinot-templates-angular ###

This library is an AngularJS module that implements a template-based textual notation for PPIs defined with PPINOT. The
tool guides the user by providing linguistic patterns according to the selection performed in the different fields.
PPIs are read and edited according to the PPINOT JSON format.

Installation
------------

PPINOT libraries are available in a Maven repository. Specifically, you must add the following text to the `pom.xml`
of your project:

```
<repositories>
    <repository>
        <id>es.us.isa</id>
        <name>ISA group</name>
        <url>http://clinker.isagroup.es/nexus/content/groups/public</url>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>es.us.isa.ppinot</groupId>
        <artifactId>ppinot-model</artifactId>
        <version>2.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>es.us.isa.ppinot</groupId>
        <artifactId>ppinot-oryx</artifactId>
        <version>2.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>es.us.isa.ppinot</groupId>
        <artifactId>ppinot-templates-angular</artifactId>
        <version>2.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

Acknowledgements
----------------
This project has been developed as part of Research Projects ISABEL, SETI, THEOS and TAPAS.