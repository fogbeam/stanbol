<p>
  Stanbol OntoNet implements the API section for managing OWL and OWL2 ontologies, in order to prepare them for consumption by reasoning services, refactorers, rule engines and the like. Ontology management in ONM is sparse and not connected: once loaded internally from their remote locations, ontologies live and are known within the realm they were loaded in. This allows loose-coupling and (de-)activation of ontologies in order to scale the data sets for reasoners to process and optimize them for efficiency. The following concepts have been introduced with the ONM:
  <ul>
    <li>
      <u><em>Ontology scope</em></u>: a "logical realm" for all the ontologies that encompass a certain CMS-related set of concepts (such as "User", "ACL", "Event", "Content", "Domain", "Reengineering", "Community", "Travelling" etc.). Scopes never inherit from each other, though they can load the same ontologies if need be.
    </li>
    <li>
      <u><em>Ontology space</em></u>: an access-restricted container for synchronized access to ontologies within a scope. The ontologies in a scope are loaded within its set of spaces. An ontology scope contains: (a) exactly one <em>core space</em>, which contains the immutable set of essential ontologies that describe the scope; (b) exactly one (possibly empty) <em>custom space</em>, which extends the core space according to specific CMS needs (e.g. the core space for the User scope may contains alignments to FOAF); (c) zero or more <em>session spaces</em>, which extend the custom space with additional models provided by end-users (e.g. the set of individuals that 'populate' a scope may be fed to OntoNet via a session space). Session spaces are mapped one-to-one with KReS sessions (see below).
    </li>
    <li>
      <em><u>OntoNet session</u></em>: a container of session spaces for all affected scopes, for stateful management of ontology networks. It is not equivalent to an HTTP session (since it can live persistently across multiple HTTP sessions), although its behaviour can reflect the one of the HTTP session that created it, if required by the implementation.
    </li>
  </ul>
</p>