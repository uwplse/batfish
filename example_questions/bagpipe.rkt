; bagpipe setup

(define (export-metric m) (lambda (r i o p ai al al* ae)
  (implies (available? ae)
    (eq? m (announcement-med (current ae))))))

(define (export-community f) (lambda (r i o p ai al al* ae)
  (implies (available? ae)
    (f (announcement-community-test (current ae) (string->symbol "65001:2"))))))

(define (export-prefix pfx) (lambda (r i o p ai al al* ae)
  (implies (and (available? al*)
                (eq? (router-addr o) (ip 2 34 201 3))
                (same-prefix? p pfx))
           (available? ae))))

(define (policy args)
  (define prop (first args))
  (cond
    [(equal? prop "export-right-metric") (export-metric 50)]
    [(equal? prop "export-wrong-metric") (export-metric 60)]
    [(equal? prop "export-right-community") (export-community identity)]
    [(equal? prop "export-wrong-community") (export-community not)]
    [(equal? prop "export-right-prefix") (export-prefix (cidr (ip 2 128 0 0) 16))]
    [(equal? prop "export-wrong-prefix") (export-prefix (cidr (ip 8 127 0 0) 16))]))

(define (driver args) 'all)

(define (as args)
  (as-from-configs 'batfish (list "configs/as2dept1")))

