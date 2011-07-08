<h4>Subresource ontonet/session</h4>
<p>Service to manage the set of active OntoNet sessions.

<h4> POST ontonet/session</h4>
<table>
  <tbody>
    <tr>
      <th>Description</th>
      <td>Service to add an ontology to a session space, creating the session if it does not exist.</td>
    </tr>
    <tr>
      <th>Request</th>
      <td>POST <code>/ontonet/session</code></td>
    </tr>
    <tr>
      <th>Parameters</th>
      <td><code>input</code>: the OWL file to be loaded. This parameter is mutually exclusive with <code>location</code>.</td>
    </tr>
    <tr>
      <th></th>
      <td><code>location</code>: the physical URL of the OWL file to be loaded. This parameter is mutually exclusive with <code>input</code>.</td>
    </tr>
    <tr>
      <th></th>
      <td><code>scope</code>: the ID of the scope whose session space will contain the ontology.</td>
    </tr>
    <tr>
      <th></th>
      <td><code>session</code>: the ID of the session to add the ontology. If it does not exist it will be created, along with corresponding session spaces for all active scopes.</td>
    </tr>
    <tr>
      <th>Produces</th>
      <td>Nothing. Returns Status 200 if successful, 500 otherwise.</td>
    </tr>
  </tbody>
</table>

<h5>Example</h5>

<pre>curl -X POST "${it.publicBaseUri}ontonet/session?scope=[scope_id]&session=[session_id]&location=[location]"</pre>


<h4> DELETE ontonet/session</h4>
<table>
  <tbody>
    <tr>
      <th>Description</th>
      <td>Service to remove an ontology from an OntoNet session, or the whole session.</td>
    </tr>
    <tr>
      <th>Request</th>
      <td>DELETE <code>/ontonet/session</code></td>
    </tr>
    <tr>
      <th>Parameters</th>
      <td><code>scope</code>: the ID of the scope.</td>
    </tr>
    <tr>
      <th></th>
      <td><code>session</code>: the ID of the session to perform the deletion on.</td>
    </tr>
    <tr>
      <th></th>
      <td><code>delete</code>: the ID of the ontology to remove from the session. This parameter is optional, if unspecified will result in the removal of the entire session.</td>
    </tr>
    <tr>
      <th>Produces</th>
      <td>Nothing. Returns Status 200 if successful, 500 otherwise.</td>
    </tr>
  </tbody>
</table>

<h5>Example</h5>

<pre>curl -X DELETE "${it.publicBaseUri}ontonet/session?scope=[scope_id]&session=[session_id]&delete=[ontology_id]"</pre>