ExampleData = Object.toJSON( 
[{"_class": "PPI", "_view": 
  {"collapsed": false}, "id": "PPI-001", "name": "Average Time of RFC Analysis", "process": "Request For Change (RFC)", "goals": ["Improve Customer Satisfaction", "Reduce RFC time-to-response"], "unit": "Working days", "source": "Event logs", "responsible": "Planning and quality manager", "informed": "CIO", "comments": "Most RFCs are created after 12.00", "definition": 
  {"_class": "Aggregated", "_view": 
    {"collapsed": false}, "function": "average", "aggregates": 
    {"_class": "Time", "from": 
      {"_class": "BPElementCondition", "_view": 
        {"collapsed": false}, "elementname": "Analyse RFC", "elementstate": "Active"}, "to": 
      {"_class": "BPElementCondition", "_view": 
        {"collapsed": false}, "elementname": "Analyse RFC", "elementstate": "Completed"}}}, "target": 
  {"_class": "LowerThan", "_view": 
    {"collapsed": false}, "value": 1}}]
);