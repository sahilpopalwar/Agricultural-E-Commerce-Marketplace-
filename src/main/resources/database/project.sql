SELECT * FROM product 
WHERE LOWER(name) LIKE LOWER('%searchTerm%') 
   OR LOWER(description) LIKE LOWER('%searchTerm%');
