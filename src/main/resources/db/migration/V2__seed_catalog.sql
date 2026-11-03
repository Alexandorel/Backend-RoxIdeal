INSERT INTO categories (name) VALUES
    ('Vopsele'),
    ('Construcții'),
    ('Izolații'),
    ('Instalații')
ON CONFLICT (name) DO NOTHING;

INSERT INTO products (name, description, price, stock, unit, category_id)
SELECT v.name, v.description, v.price, v.stock, v.unit, c.id
FROM (VALUES
    ('Vopsea lavabilă albă 15 L',      'Vopsea interior, lavabilă, acoperire 140 mp',  189.90,  40, 'BUC', 'Vopsele'),
    ('Email alchidic negru 0,75 L',     'Pentru lemn și metal, interior/exterior',        42.50,  25, 'BUC', 'Vopsele'),
    ('Diluant universal',               'Pentru vopsele alchidice și curățare scule',     18.00,  60, 'L',   'Vopsele'),
    ('Ciment Portland 40 kg',           'CEM II/A-LL 42,5R',                               38.90, 120, 'BUC', 'Construcții'),
    ('Nisip spălat 0-4 mm',             'Pentru betoane și tencuieli, vândut la kg',        0.12, 5000, 'KG',  'Construcții'),
    ('Polistiren expandat EPS 80, 10 cm','Izolație termică fațade',                         32.50, 300, 'MP',  'Izolații'),
    ('Vată minerală 5 cm',              'Izolație termică și fonică, rolă',                14.90,   0, 'MP',  'Izolații'),
    ('Țeavă PPR 20 mm',                 'Apă caldă/rece, PN20',                             6.50, 800, 'M',   'Instalații'),
    ('Cablu electric MYYM 3x2,5',       'Cupru, pentru prize și circuite de putere',        7.80, 1000, 'M',   'Instalații')
) AS v(name, description, price, stock, unit, category)
JOIN categories c ON c.name = v.category;
